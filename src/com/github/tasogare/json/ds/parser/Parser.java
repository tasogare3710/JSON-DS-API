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
 * 構文解析を行いASTを返すパーサー。このパーサは主に意味解析を行うJSON-DSプロセッサによって利用される。
 * 
 * @author tasogare
 *
 */
public class Parser {

    protected final TokenStream ts;
    protected final String sourceName;

    public Parser(final TokenStream ts, final String sourceName) {
        this.ts = ts;
        this.sourceName = sourceName;
    }

    public <D extends AstNode & DirectiveNode<D>> ProgramNode<D> parse() throws ParserException {
        return this. <D> parseProgram();
    }

    protected List<PragmaNode> parsePragmas() {
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

    protected PragmaNode parsePragma() {
        //@formatter:off
        // # include pragma proposal
        // Pragma
        //      UsePragma Semicolon
        //      IncludePragma Semicolon
        //
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!t.equals(UsePragma, EscapedUsePragma, IncludePragma, EscapedIncludePragma)) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }

        return pragma;
    }

    /**
     * @return
     */
    protected PragmaNode parseUsePragma() {
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
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        ts.pushPendding(t);
        
        final List<IdentifierNode> items = new ArrayList<>();
        int count = 0;
        while (true) {
            t = ts.scanTokenWithoutOf(Comment);
            if (t == Eof) {
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
            }
            // PragmaItem
            final IdentifierNode ident = parseIdentifier();
            // "use" "standard" [ContextuallyReservedIdentifier]
            // "use" "strict" [ContextuallyReservedIdentifier]
            if (ident instanceof ContextuallyReservedIdentifierNode) {
                final ContextuallyReservedIdentifierNode crident = (ContextuallyReservedIdentifierNode) ident;
                if (crident.isStandard() || crident.isStrict()) {
                    // TODO: 11.6.1.1 - Static Semantics: Early Errors
                    if (count != 0) {
                        throw new ParserException(new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(),
                            sourceName, ts.source()));
                    }else{
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
    protected PragmaNode parseUsePragmaItems() {
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == SemiColon) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        ts.pushPendding(t);

        final List<IdentifierNode> items = new ArrayList<>();
        while (true) {
            t = ts.scanTokenWithoutOf(Comment);
            if (t == Eof) {
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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

    protected PragmaNode parseIncludePragma() {
        //@formatter:off
        // IncludePragma
        //      "include"  IncludePragmaItem
        //@formatter:on
        final StringLiteralNode item = parseIncludePragmaItem();
        final PragmaNode pragma = new<StringLiteralNode> PragmaNode(0, ts.position(), "include", item);
        return pragma;
    }

    protected StringLiteralNode parseIncludePragmaItem() {
        //@formatter:off
        // IncludePragmaItem
        //      StringLiteral
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t == SemiColon) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        ts.pushPendding(t);

        t = ts.scanTokenWithoutOf(Comment);
        if (t == Eof) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        // PragmaItem
        if (t != StringLiteral) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        final String str = ts.asStringLiteral();
        return new StringLiteralNode(0, ts.position(), str);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <D extends AstNode & DirectiveNode<D>> ProgramNode<D> parseProgram() {
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

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseDirective() {
        return parseAttributedDirective();
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseAttributedDirective() {
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
        throw new ParserException(
            new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeDefinitionNode<T> parseTypeDefinition() {
        //@formatter:off
        // TypeDefinition
        //      "type" Identifier TypeInitialisation
        //@formatter:on
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!(t == TypeOperator || t == EscapedTypeOperator)) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }

        t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (!(t == Identifier || t == EscapedIdentifier)) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeExpressionNode<T> parseTypeInitialisation() {
        //@formatter:off
        // TypeInitialisation
        //      "=" TypeExpression
        //@formatter:on
        final Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        if (t != Assign) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        return parseTypeExpression();
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> TypeExpressionNode<T> parseTypeExpression() {
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
    protected <T extends AstNode & BasicTypeExpressionNode<T>> T parseBasicTypeExpression() {
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
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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

    protected <T extends AstNode & BasicTypeExpressionNode<T>> UnionTypeNode<T> parseUnionType() {
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
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> List<TypeExpressionNode<T>> parseTypeUnionList() {
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
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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

    protected <T extends AstNode & BasicTypeExpressionNode<T>> RecordTypeNode<T> parseRecordType() {
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
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> List<FieldTypeNode<T>> parseFieldTypeList() {
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
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        while (true) {
            switch (t) {
            case Eof:
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
            case StringLiteral:
                list.add(parseFieldType());
                t = ts.scanTokenWithoutOf(Comment);
                if (t == Comma || t == LineTerminator || t == RBrace) {
                    continue;
                }
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
            case LineTerminator:
            case Comma:
                t = ts.scanTokenWithoutOf(Comment);
                continue;
            case RBrace:
                return list;
            default:
                throw new ParserException(
                    new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
            }
        }
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> FieldTypeNode<T> parseFieldType() {
        //@formatter:off
        // FieldType
        //      FieldName : TypeExpression
        //@formatter:on
        assert ts.getCurrentToken() == StringLiteral;
        final String name = ts.asStringLiteral();
        Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        // JSON-DSでは型注釈の付かないフィールドは許されない
        if (t != Colon) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        final TypeExpressionNode<T> te = parseTypeExpression();

        final FieldNameNode.StringLiteral fn = AstContext.newStringLiteralFieldName(ts.position(), ts.position(), name);
        return new FieldTypeNode<T>(ts.position(), ts.position(), fn, te);
    }

    protected <T extends AstNode & BasicTypeExpressionNode<T>> ArrayTypeNode<T> parseArrayType() {
        //@formatter:off
        // ArrayType
        //      "[" ElementTypeList "]"
        //@formatter:on
        assert ts.getCurrentToken() == LBracket;

        final Token t = ts.scanTokenWithoutOf(Comment, LineTerminator);
        // JSON-DSでは [,,,]や[ , foo]という記述は許可されない
        if (t == Comma) {
            throw new ParserException(
                new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
        }
        ts.pushPendding(t);
        return parseElementTypeList();
    }

    /**
     * 
     * @return
     */
    protected <T extends AstNode & BasicTypeExpressionNode<T>> ArrayTypeNode<T> parseElementTypeList() {
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
                        throw new ParserException(new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(),
                            sourceName, ts.source()));
                    }
                } else if (t != RBracket) {
                    throw new ParserException(
                        new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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
                    throw new ParserException(
                        new SourceInfo(ts.row(), ts.column(), ts.lineStart(), ts.position(), sourceName, ts.source()));
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
