// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static com.github.tasogare.json.ds.parser.Token.Assign;
import static com.github.tasogare.json.ds.parser.Token.Colon;
import static com.github.tasogare.json.ds.parser.Token.Comma;
import static com.github.tasogare.json.ds.parser.Token.Comment;
import static com.github.tasogare.json.ds.parser.Token.Eof;
import static com.github.tasogare.json.ds.parser.Token.EscapedIdentifier;
import static com.github.tasogare.json.ds.parser.Token.EscapedIncludePragma;
import static com.github.tasogare.json.ds.parser.Token.EscapedTypeOperator;
import static com.github.tasogare.json.ds.parser.Token.EscapedUsePragma;
import static com.github.tasogare.json.ds.parser.Token.Identifier;
import static com.github.tasogare.json.ds.parser.Token.IncludePragma;
import static com.github.tasogare.json.ds.parser.Token.LBrace;
import static com.github.tasogare.json.ds.parser.Token.LBracket;
import static com.github.tasogare.json.ds.parser.Token.LParen;
import static com.github.tasogare.json.ds.parser.Token.LineTerminator;
import static com.github.tasogare.json.ds.parser.Token.NotNullable;
import static com.github.tasogare.json.ds.parser.Token.Nullable;
import static com.github.tasogare.json.ds.parser.Token.Or;
import static com.github.tasogare.json.ds.parser.Token.RBrace;
import static com.github.tasogare.json.ds.parser.Token.RBracket;
import static com.github.tasogare.json.ds.parser.Token.RParen;
import static com.github.tasogare.json.ds.parser.Token.SemiColon;
import static com.github.tasogare.json.ds.parser.Token.StringLiteral;
import static com.github.tasogare.json.ds.parser.Token.TripleDot;
import static com.github.tasogare.json.ds.parser.Token.TypeOperator;
import static com.github.tasogare.json.ds.parser.Token.UsePragma;

import java.util.ArrayList;
import java.util.List;

import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.AnyTypeNode;
import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstContext;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.ContextuallyReservedIdentifierNode;
import com.github.tasogare.json.ds.internal.ast.FieldTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.DirectiveNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;

/**
 * FIXME: ASTノードの位置情報を正しく与えなければならない
 * 構文解析を行いASTを返すパーサー。このパーサは主に意味解析を行うJSON-DSプロセッサによって利用される。
 * 
 * @author tasogare
 *
 */
public class Parser {

    protected final TokenStream ts;

    public Parser(final TokenStream ts) {
        this.ts = ts;
    }

    public <D extends AstNode & DirectiveNode<D>> ProgramNode<D> parse() throws StaticSemanticsException {
        return this. <D> parseProgram();
    }

    /**
     * @return
     */
    public SourceInfo createCurrentSourceInfo() {
        return ts.createCurrentSourceInfo();
    }

    protected List<PragmaNode> parsePragmas() throws StaticSemanticsException {
        //@formatter:off
        // Pragmas
        //      «empty»
        //      PragmasPrefix Pragma
        //
        // PragmasPrefix
        //      «empty»
        //      PragmasPrefix Pragma
        //@formatter:on
        final ArrayList<PragmaNode> pragmas = new ArrayList<>();
        Token t;
        while ((t = ts.scanTokenWithoutOf(Comment, LineTerminator)).isPragma()) {
            ts.pushPendding(t);
            pragmas.add(parsePragma());
        }
        ts.pushPendding(t);
        return pragmas;
    }

    protected PragmaNode parsePragma() throws StaticSemanticsException {
        //@formatter:off
        // # include pragma proposal
        // Pragma
        //      UsePragma Semicolon
        //      IncludePragma Semicolon
        //
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!t.equals(UsePragma, EscapedUsePragma, IncludePragma, EscapedIncludePragma)) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }

        final PragmaNode pragma;
        if (t.equals(UsePragma, EscapedUsePragma)) {
            pragma = parseUsePragma();
        } else {
            pragma = parseIncludePragma();
        }

        t = ts.scanTokenWithoutOf(Comment);
        autoSemicolonInsertion(t);

        t = ts.scanTokenWithoutOf(Comment);
        if (t == Eof || t != SemiColon) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }

        return pragma;
    }

    /**
     * @return
     * @throws StaticSemanticsException
     */
    protected PragmaNode parseUsePragma() throws StaticSemanticsException {
        //@formatter:off
        // UsePragma
        //      "use"  "standard" [ContextuallyReservedIdentifier]
        //      "use"  "strict"   [ContextuallyReservedIdentifier]
        //      "use"  UsePragmaItems
        //
        // UsePragmaItems
        //      UsePragmaItem
        //      UsePragmaItems  ","  UsePragmaItem
        // 
        // UsePragmaItem
        //      Identifier
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == SemiColon) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        ts.pushPendding(t);

        final List<IdentifierNode> items = new ArrayList<>();
        int count = 0;
        while (true) {
            t = ts.scanTokenWithoutOf(Comment);
            if (t == Eof) {
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            }
            // PragmaItem
            final IdentifierNode ident = parseIdentifier();
            // "use" "standard" [ContextuallyReservedIdentifier]
            // "use" "strict" [ContextuallyReservedIdentifier]
            if (ident instanceof ContextuallyReservedIdentifierNode) {
                final ContextuallyReservedIdentifierNode crident = (ContextuallyReservedIdentifierNode) ident;
                if (crident.isStandard() || crident.isStrict()) {
                    if (count != 0) {
                        throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
                    } else {
                        return new<ContextuallyReservedIdentifierNode> PragmaNode(0, ts.position(), "use", crident);
                    }
                }
                // 今のところここで有効なContextuallyReservedIdentifierはstandardとstrictだけ
                throw new AssertionError(ident.getString());
            } else {
                items.add(ident);
            }

            t = ts.scanTokenWithoutOf(Comment);
            if (t == SemiColon || t == LineTerminator) {
                ts.pushPendding(t);
                break;
            }
            count++;
        }
        return new<IdentifierNode> PragmaNode(0, ts.position(), "use", items);
    }

    @Deprecated
    protected PragmaNode parseUsePragmaItems() throws StaticSemanticsException {
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == SemiColon) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        ts.pushPendding(t);

        final List<IdentifierNode> items = new ArrayList<>();
        while (true) {
            t = ts.scanTokenWithoutOf(Comment);
            if (t == Eof) {
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            }
            // PragmaItem
            items.add(parseIdentifier());

            t = ts.scanTokenWithoutOf(Comment);
            if (t == SemiColon || t == LineTerminator) {
                ts.pushPendding(t);
                break;
            }
        }
        return new<IdentifierNode> PragmaNode(0, ts.position(), "use", items);
    }

    protected PragmaNode parseIncludePragma() throws StaticSemanticsException {
        //@formatter:off
        // IncludePragma
        //      "include"  IncludePragmaItem
        //@formatter:on
        final StringLiteralNode item = parseIncludePragmaItem();
        final PragmaNode pragma = new<StringLiteralNode> PragmaNode(0, ts.position(), "include", item);
        return pragma;
    }

    protected StringLiteralNode parseIncludePragmaItem() throws StaticSemanticsException {
        //@formatter:off
        // IncludePragmaItem
        //      StringLiteral
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == SemiColon) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        ts.pushPendding(t);

        t = ts.scanTokenWithoutOf(Comment);
        if (t == Eof) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        // PragmaItem
        if (t != StringLiteral) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        final String str = ts.asStringLiteral();
        return new StringLiteralNode(0, ts.position(), str);
    }

    /**
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    @SuppressWarnings("unchecked")
    protected <D extends AstNode & DirectiveNode<D>> ProgramNode<D> parseProgram() throws StaticSemanticsException {
        //@formatter:off
        // Program
        //      Pragmas  Directives
        //
        // Directives
        //      «empty»
        //      DirectivesPrefix Directive
        //
        // DirectivesPrefix
        //      «empty»
        //      DirectivesPrefix Directive
        //@formatter:on
        final List<PragmaNode> pragmas = parsePragmas();

        final ArrayList<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> directives = new ArrayList<>();
        Token t;
        while ((t = ts.scanTokenWithoutOf(Comment, LineTerminator)) != Eof) {
            ts.pushPendding(t);
            directives.add(parseDirective());
        }
        final ProgramNode<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> program = new ProgramNode<>(0,
            ts.length(), directives, pragmas);
        return (ProgramNode<D>) program;
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseDirective() throws StaticSemanticsException {
        return parseAttributedDirective();
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseAttributedDirective() throws StaticSemanticsException {
        //@formatter:off
        // AttributedDirective
        //      TypeDefinition Semicolon
        //@formatter:on
        final TypeDefinitionNode<T> td = parseTypeDefinition();

        Token t = ts.scanTokenWithoutOf(Comment);
        autoSemicolonInsertion(t);

        t = ts.scanTokenWithoutOf(Comment);
        if (t.equals(Eof, SemiColon)) {
            // 一度EOFを返したらそれ以降は何度でもEOFを返すのでpenddingは必要ない。
            return td;
        }
        throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseTypeDefinition() throws StaticSemanticsException {
        //@formatter:off
        // TypeDefinition
        //      "type" Identifier TypeInitialisation
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!(t == TypeOperator || t == EscapedTypeOperator)) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }

        t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!(t == Identifier || t == EscapedIdentifier)) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        final IdentifierNode identifier = parseIdentifier();
        TypeExpressionNode<T> te = parseTypeInitialisation();
        return new TypeDefinitionNode<T>(ts.position(), ts.position(), identifier, te);
    }

    /**
     * 
     * @return {@literal ContextuallyReservedIdentifier}の場合は
     *         {@link ContextuallyReservedIdentifier}を返す
     */
    protected IdentifierNode parseIdentifier() {
        assert ts.getCurrentToken() == Identifier || ts.getCurrentToken() == EscapedIdentifier;

        final String s = (ts.getCurrentToken() == Identifier) ? ts.asIdentifier() : ts.asEscapedIdentifier();
        final IdentifierNode identifier;
        if (!("standard".equals(s) || "strict".equals(s))) {
            identifier = new IdentifierNode(ts.position(), ts.position(), s);
        } else {
            identifier = new ContextuallyReservedIdentifierNode(ts.position(), ts.position(), s);
        }
        return identifier;
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeExpressionNode<T> parseTypeInitialisation() throws StaticSemanticsException {
        //@formatter:off
        // TypeInitialisation
        //      "=" TypeExpression
        //@formatter:on
        final Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t != Assign) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        return parseTypeExpression();
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeExpressionNode<T> parseTypeExpression() throws StaticSemanticsException {
        //@formatter:off
        // TypeExpression
        //      BasicTypeExpression
        //      BasicTypeExpression "?"
        //      BasicTypeExpression "!"
        //@formatter:on
        final T type = parseBasicTypeExpression();

        final Token t = ts.scanTokenWithoutOf(Comment);
        final TypeExpressionNode.Nullability nullability;
        if (t == Nullable) {
            nullability = TypeExpressionNode.Nullability.Nullable;
        } else if (t == NotNullable) {
            nullability = TypeExpressionNode.Nullability.NonNullable;
        } else {
            nullability = TypeExpressionNode.Nullability.Absent;
            ts.pushPendding(t);
        }
        return new TypeExpressionNode<T>(ts.position(), ts.position(), type, nullability);
    }

    @SuppressWarnings("unchecked")
    protected <T extends AstNode & BasicTypeExpressionNode<T>> T parseBasicTypeExpression() throws StaticSemanticsException {
        //@formatter:off
        // BasicTypeExpression
        //      "*"
        //      "null"
        //      TypeName
        //      UnionType
        //      RecordType
        //      ArrayType
        //@formatter:on
        final Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        switch (t) {
        case Any:
            return (T) new AnyTypeNode(ts.position(), ts.position());
        case Null:
            return (T) new NullLiteralNode(ts.position(), ts.position());
        case Identifier:
        case EscapedIdentifier:
            return (T) parseTypeName();
        case LParen:
            return (T) parseUnionType();
        case LBrace:
            return (T) parseRecordType();
        case LBracket:
            return (T) parseArrayType();
        default:
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
    }

    protected TypeNameNode parseTypeName() {
        //@formatter:off
        // TypeName
        //      NameExpression
        //@formatter:on
        final NameExpressionNode ne = parseNameExpression();
        return new TypeNameNode(ts.position(), ts.position(), ne);
    }

    protected NameExpressionNode parseNameExpression() {
        //@formatter:off
        //NameExpression
        //      Identifier
        //@formatter:on
        assert ts.getCurrentToken() == Identifier || ts.getCurrentToken() == EscapedIdentifier;
        final IdentifierNode identifier = parseIdentifier();
        return new NameExpressionNode(ts.position(), ts.position(), identifier);
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> UnionTypeNode<T> parseUnionType() throws StaticSemanticsException {
        //@formatter:off
        // UnionType
        //      "(" TypeUnionList ")"
        //@formatter:on
        assert ts.getCurrentToken() == LParen;
        final List<TypeExpressionNode<T>> list = parseTypeUnionList();
        return new UnionTypeNode<T>(ts.position(), ts.position(), list);
    }

    /**
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> List<TypeExpressionNode<T>> parseTypeUnionList() throws StaticSemanticsException {
        //@formatter:off
        // TypeUnionList
        //      «empty»
        //      NonemptyTypeUnionList
        //
        // NonemptyTypeUnionList
        //      TypeExpression
        //      TypeExpression | NonemptyTypeUnionList
        //@formatter:on
        final List<TypeExpressionNode<T>> list = new ArrayList<>();
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        // TypeExpression | NonemptyTypeUnionList なので空の | は無効
        if (t == Or) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }

        if (t == RParen) {
            return list;
        } else {
            ts.pushPendding(t);
        }

        while (true) {
            TypeExpressionNode<T> te = parseTypeExpression();
            list.add(te);
            t = ts.scanTokenWithoutOf(Comment, LineTerminator);
            if (t == Eof) {
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            }
            if (t == Or) {
                continue;
            }
            if (t == RParen) {
                break;
            }
        }
        return list;
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> RecordTypeNode<T> parseRecordType() throws StaticSemanticsException {
        //@formatter:off
        // RecordType
        //      "{" FieldTypeList "}"
        //@formatter:on
        assert ts.getCurrentToken() == LBrace;
        List<FieldTypeNode<T>> list = parseFieldTypeList();
        assert ts.getCurrentToken() == RBrace;
        return new RecordTypeNode<T>(ts.position(), ts.position(), list);
    }

    /**
     * @return
     * @throws StaticSemanticsException 
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> List<FieldTypeNode<T>> parseFieldTypeList() throws StaticSemanticsException {
        //@formatter:off
        // FieldTypeList
        //      «empty»
        //      FieldType
        //      FieldType , FieldTypeList
        //@formatter:on
        final ArrayList<FieldTypeNode<T>> list = new ArrayList<>();
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == Comma) {
            // 空のカンマは許されない
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        while (true) {
            switch (t) {
            case Eof:
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            case StringLiteral:
                list.add(parseFieldType());
                t = ts.scanTokenWithoutOf(Comment);
                if (t == Comma || t == LineTerminator || t == RBrace) {
                    continue;
                }
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            case LineTerminator:
            case Comma:
                t = ts.scanTokenWithoutOf(Comment);
                continue;
            case RBrace:
                return list;
            default:
                throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
            }
        }
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> FieldTypeNode<T> parseFieldType() throws StaticSemanticsException {
        //@formatter:off
        // FieldType
        //      FieldName : TypeExpression
        //@formatter:on
        assert ts.getCurrentToken() == StringLiteral;
        final String name = ts.asStringLiteral();
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        // JSON-DSでは型注釈の付かないフィールドは許されない
        if (t != Colon) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        final TypeExpressionNode<T> te = parseTypeExpression();

        final FieldNameNode.StringLiteral fn = AstContext.newStringLiteralFieldName(ts.position(), ts.position(), name);
        return new FieldTypeNode<T>(ts.position(), ts.position(), fn, te);
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> ArrayTypeNode<T> parseArrayType() throws StaticSemanticsException {
        //@formatter:off
        // ArrayType
        //      "[" ElementTypeList "]"
        //@formatter:on
        assert ts.getCurrentToken() == LBracket;

        final Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        // JSON-DSでは [,,,]や[ , foo]という記述は許可されない
        if (t == Comma) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        ts.pushPendding(t);
        return parseElementTypeList();
    }

    /**
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> ArrayTypeNode<T> parseElementTypeList() throws StaticSemanticsException {
        //@formatter:off
        // ElementTypeList
        //      «empty»
        //      VariableLengthElementTypeList
        //      FixedLengthElementTypeList
        //
        // VariableLengthElementTypeList
        //      "…" TypeExpression
        //      "…" TypeExpression ","
        //      TypeExpression "," VariableLengthElementTypeList
        //
        // FixedLengthElementTypeList
        //      TypeExpression
        //      TypeExpression ","
        //      TypeExpression "," FixedLengthElementTypeList
        //@formatter:on
        final ArrayList<TypeExpressionNode<T>> list = new ArrayList<>();
        TypeExpressionNode<T> variableType = null;
        while (true) {
            Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
            if (t == TripleDot) {
                variableType = parseTypeExpression();
                t = ts.scanTokenWithoutOf(Comment, LineTerminator);
                if (t == Comma) {
                    t = ts.scanTokenWithoutOf(Comment, LineTerminator);
                    if (t != RBracket) {
                        throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
                    }
                } else if (t != RBracket) {
                    throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
                }
                return new ArrayTypeNode<T>(ts.position(), ts.position(), list, variableType);
            } else if (t == RBracket) {
                break;
            } else {
                ts.pushPendding(t);
                list.add(parseTypeExpression());
                t = ts.scanTokenWithoutOf(Comment, LineTerminator);
                if (t == Comma) {
                    t = ts.scanTokenWithoutOf(Comment, LineTerminator);
                    if (t == RBracket) {
                        break;
                    }
                    ts.pushPendding(t);
                    continue;
                } else if (t != RBracket) {
                    throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
                }
                break;
            }
        }
        return new ArrayTypeNode<T>(ts.position(), ts.position(), list, variableType);
    }

    /**
     * 
     * @param t
     */
    private void autoSemicolonInsertion(Token t) {
        if (t == LineTerminator) {
            ts.pushPendding(SemiColon);
        } else {
            ts.pushPendding(t);
        }
    }
}
