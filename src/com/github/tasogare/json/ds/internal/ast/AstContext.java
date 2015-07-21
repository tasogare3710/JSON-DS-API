// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;

public class AstContext {

    private AstContext() {
    }

    public static TypeNameNode newTypeName(int start, int end, String identifier){
        return new TypeNameNode(start, end, new NameExpressionNode(start, end, new IdentifierNode(start, end, identifier)));
    }

    public static <T extends AstNode & BasicTypeExpressionNode<T>> FieldTypeNode<T> newFieldType(int startFt, int endFt,
            int startFn, int endFn, String label, TypeExpressionNode<T> te)
    {
        final FieldNameNode.StringLiteral fn = newStringLiteralFieldName(startFn, endFn, label);
        return new FieldTypeNode<T>(startFt, endFt, fn, te);
    }

    public static FieldNameNode.StringLiteral newStringLiteralFieldName(int start, int end, String str){
        return new FieldNameNode.StringLiteral(start, end, new StringLiteralNode(start, end, str));
    }
}
