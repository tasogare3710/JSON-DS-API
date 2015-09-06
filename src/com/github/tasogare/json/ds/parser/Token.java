// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

/**
 * @author tasogare
 *
 */
public enum Token {
    Null("null"){
        @Override public Token toEscaped() {
            return Token.EscapedNull;
        }
    },
    Nullable("?"),
    NotNullable("!"),
    Any("*"),
    Assign("="),
    Or("|"),
    LParen("("),
    RParen(")"),
    LBracket("["),
    RBracket("]"),
    LBrace("{"),
    RBrace("}"),
    Comma(","),
    Colon(":"),
    SemiColon(";"),
    TripleDot("..."),
    TypeOperator("type"){
        @Override Token toEscaped() {
            return Token.EscapedTypeOperator;
        }
    },
    UsePragma("use"){
        @Override Token toEscaped() {
            return Token.EscapedUsePragma;
        }

        @Override public boolean isPragma(){
            return true;
        }
    },
    IncludePragma("include"){
        @Override Token toEscaped() {
            return Token.EscapedIncludePragma;
        }

        @Override public boolean isPragma(){
            return true;
        }
    },
    Identifier("<identifier>"){
        @Override public Token toEscaped() {
            return Token.EscapedIdentifier;
        }
    },
    StringLiteral("<string-literal>"),
    EscapedNull("<escaped-null>"),
    EscapedIdentifier("<escaped-identifier>"),
    EscapedTypeOperator("<escaped-type>"),
    EscapedUsePragma("<escaped-use>"){
        @Override public boolean isPragma(){
            return true;
        }
    },
    EscapedIncludePragma("<escaped-include>"){
        @Override public boolean isPragma(){
            return true;
        }
    },
    Comment("<comment>"),
    LineTerminator("<line-terminator>"),
    Eof("<eof>");

    private final String name;
    private Token(final String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return getName();
    }

    Token toEscaped() {
        throw new AssertionError();
    }

    /**
     * 実験的
     * @return トークンがプラグマなら{@code true}
     */
    public boolean isPragma(){
        return false;
    }

    /**
     * @param expected
     * @return
     */
    boolean equals(Token... expected) {
        assert expected.length > 0;
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] == this) {
                return true;
            }
        }
        return false;
    }
}
