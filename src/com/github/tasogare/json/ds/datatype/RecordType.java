// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RecordType implements StructuralType {

    public static final class Field implements Type {
        private final String name;
        private final Type type;
        public Field(final String name, final Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }
        public Type getType() {
            return type;
        }

        @Override
        public boolean isTypeOf(Type t) {
            if(! (t instanceof Field) ){
                return false;
            }
            final Field other = (Field) t;
            return this.name.equals(other.name) && this.type.isTypeOf(other.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj){
                return true;
            }
            if (obj == null || getClass() != obj.getClass()){
                return false;
            }
            final Field other = (Field) obj;
            return this.name.equals(other.name) && this.type.equals(other.type);
        }

        @Override
        public String toString() {
            return "Field [name=" + name + ", type=" + type + "]";
        }
    }

    private final HashSet<Field> fields;
    public RecordType(final Set<Field> fields) {
        this.fields = new HashSet<>(fields);
    }

    @Override
    public boolean isTypeOf(Type t) {
        if(t instanceof ReferenceType){
            t = ((ReferenceType)t).getValue();
        }

        if(t instanceof AnyType){
            return true;
        }

        if(! (t instanceof RecordType)){
            return false;
        }

        final RecordType other = (RecordType) t;
        if(other.fields.size() != this.fields.size()){
            return false;
        }

        final HashSet<RecordType.Field> matchs = new HashSet<>(other.fields.size());
        for(final RecordType.Field selfF : this.fields){
            for(final RecordType.Field otherF : other.fields){
                if( selfF.isTypeOf(otherF)){
                    matchs.add(selfF);
                }
            }
        }
        return matchs.size() == other.fields.size();
    }

    public HashSet<Field> getFields() {
        return fields;
    }
}
