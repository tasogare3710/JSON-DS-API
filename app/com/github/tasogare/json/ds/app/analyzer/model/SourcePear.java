// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer.model;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SourcePear {

    private final ObjectProperty<File> schema;
    private final ObjectProperty<File> json;

    public SourcePear() {
        this(null, null);
    }

    public SourcePear(File schema) {
        this(schema, null);
    }

    public SourcePear(File schema, File json) {
        this.schema = new SimpleObjectProperty<>(this, "schema", schema);
        this.json = new SimpleObjectProperty<>(this, "json", json);
    }

    public final ObjectProperty<File> schemaProperty() {
        return schema;
    }

    public final File getSchema() {
        return schemaProperty().get();
    }

    public final void setSchema(final File schema) {
        schemaProperty().set(schema);
    }

    public final ObjectProperty<File> jsonProperty() {
        return json;
    }

    public final File getJson() {
        return jsonProperty().get();
    }

    public final void setJson(final File json) {
        jsonProperty().set(json);
    }

}
