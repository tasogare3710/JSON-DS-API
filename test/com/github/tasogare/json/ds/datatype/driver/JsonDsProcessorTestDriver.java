// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import static com.github.tasogare.json.ds.datatype.Intrinsics.anyType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.newNullable;
import static com.github.tasogare.json.ds.datatype.Intrinsics.nonNullableAnyType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.nullType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.undefindType;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

import com.github.tasogare.json.ds.JsonDsException;
import com.github.tasogare.json.ds.JsonDsException.StandardErrors;
import com.github.tasogare.json.ds.datatype.AnyType;
import com.github.tasogare.json.ds.datatype.ArrayType;
import com.github.tasogare.json.ds.datatype.RecordType;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.UnionType;
import com.github.tasogare.json.ds.internal.ast.AnyTypeNode;
import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.ContextuallyReservedIdentifierNode;
import com.github.tasogare.json.ds.internal.ast.FieldTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.SemicolonNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.DirectiveNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

/**
 * non-hositable版のプロセッサ　テスト・ドライバの実装
 * 
 * とくに説明がない限りこのクラスのメソッドは戻り値が必要ないメソッドは{@code null}を返します。
 * @author tasogare
 *
 */
public class JsonDsProcessorTestDriver implements NodeVisitor<Type>, DatatypeSchemaProcessorTestDriver {

    protected Variant complianceVariant;
    private final JsonMetaObjectTestDriver metaObjects;

    public JsonDsProcessorTestDriver(JsonMetaObjectTestDriver metaObjects) {
        this.metaObjects = metaObjects;
    }

    public JsonDsProcessorTestDriver() {
        this(new JsonMetaObjectTestDriver());
    }

    @Override
    public Type visit(AnyTypeNode node) {
        return anyType;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(ArrayTypeNode<T> node) {
        final ArrayList<Type> fixedLengthElements = new ArrayList<>();
        for(final TypeExpressionNode<T> e : node.getElementTypeList()){
            fixedLengthElements.add(e.accept(this));
        }
        final TypeExpressionNode<T> vaTypes = node.getVariableArrayType();
        final Type variableLengthElements = vaTypes == null ? null : vaTypes.accept(this);
        ArrayType array = new ArrayType(fixedLengthElements, variableLengthElements);
        return array;
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(FieldNameNode.StringLiteral node) {
        throw new AssertionError("unused");
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(FieldTypeNode<T> node) {
        final String name = ((FieldNameNode.StringLiteral)node.getName()).getString();
        final Type type = node.getType().accept(this);
        final RecordType.Field field = new RecordType.Field(name, type);
        return field;
    }

    @Override
    public Type visit(IdentifierNode node) {
        metaObjects.registerMetaObject(node.getString(), undefindType);
        return null;
    }

    @Override
    public Type visit(NameExpressionNode node) {
        final String name = node.getString();
        return metaObjects.getMetaObject(name);
    }

    @Override
    public Type visit(NullLiteralNode node) {
        return nullType;
    }

    @Override
    public Type visit(PragmaNode node) {
        //FIXME: use pragma決め打ち
        for(final IdentifierNode iden : node.getPragmaItems()){
            if(iden instanceof ContextuallyReservedIdentifierNode){
                if (((ContextuallyReservedIdentifierNode) iden).isStandard()) {
                    complianceVariant = Variant.Standard;
                    return null;
                }
            }
        }
        throw new JsonDsException("standard variantのみ利用できます", StandardErrors.SyntaxError);
    }

    @Override
    public <D extends AstNode & DirectiveNode<D>> Type visit(ProgramNode<D> node) {
        if(node.getPragmas().isEmpty()){
            throw new JsonDsException("Useプラグマがありません", StandardErrors.SyntaxError);
        }
        for(final PragmaNode p : node.getPragmas()){
            p.accept(this);
        }
        for(final D d : node.getDirectives()){
            d.accept(this);
        }
        return null;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(RecordTypeNode<T> node) {
        HashSet<RecordType.Field> fields = new HashSet<>();
        for(final FieldTypeNode<T> f : node.getFieldTypeList()){
            fields.add((RecordType.Field) f.accept(this));
        }
        return new RecordType(fields);
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(SemicolonNode node) {
        throw new AssertionError("unused");
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(StringLiteralNode node) {
        throw new AssertionError("unused");
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeDefinitionNode<T> node) {
        node.getIdentifier().accept(this);
        Type init = node.getTypeInitialization().accept(this);
        metaObjects.registerMetaObject(node.getIdentifier().getString(), init);
        return null;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeExpressionNode<T> node) {
        final T bte = node.getBasicTypeExpression();
        final Type b = bte.accept(this);

        switch (node.getNullability()) {
        case Nullable:
            return newNullable(b);
        case NonNullable:
            return b instanceof AnyType ? nonNullableAnyType : b;
        case Absent:
            return b;
        default:
            throw new AssertionError();
        }
    }

    @Override
    public Type visit(TypeNameNode node) {
        final Type metaObject = node.getNameExpression().accept(this);
        if(metaObject == null){
            throw new JsonDsException(node.getString() + "が未定義です", StandardErrors.ReferenceError);
        }
        return metaObject;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(UnionTypeNode<T> node) {
        HashSet<Type> members = new HashSet<>();
        for(final TypeExpressionNode<T> te : node.getTypeUnionList()){
            members.add(te.accept(this));
        }
        return new UnionType(members);
    }

    @Override
    public void process(String jsds, String sourceName) {
        final Source source = new Source(jsds);
        final TokenStream ts = new TokenStream(source);
        final Parser parser = new Parser(ts, sourceName);
        final ProgramNode<?> p = parser.parse();
        visit(p);
    }

    @Override
    public void process(Reader jsds, String sourceName) {
        final Source source = new Source(jsds);
        final TokenStream ts = new TokenStream(source);
        final Parser parser = new Parser(ts, sourceName);
        final ProgramNode<?> p = parser.parse();
        visit(p);
    }

    /**
     * 
     * @return JSON-DSファイルが準拠しているJSON-DS Variantを返す
     */
    public final Variant getComplianceVariant() {
        return complianceVariant;
    }

    public final JsonMetaObjectTestDriver getMetaObjects() {
        return metaObjects;
    }

}
