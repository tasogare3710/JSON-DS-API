// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import com.github.tasogare.json.ds.MetaObject;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.driver.JsonDsProcessorHoistableTestDriver;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class JsonValidateService extends Service<Boolean> {

    private final SimpleObjectProperty<File> schema;
    private final SimpleObjectProperty<File> json;

    public JsonValidateService(File schema, File json) {
        this.schema = new SimpleObjectProperty<>(this, "schema", schema);
        this.json = new SimpleObjectProperty<>(this, "json", json);
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
                try (BufferedReader r = new BufferedReader(new FileReader(getSchema()))) {
                    processor.process(r, getSchema().toURI().toURL());
                }
                updateMessage("schema processed");
                updateProgress(1, 3);

                final JsonStructure structure;
                try (BufferedReader r = new BufferedReader(new FileReader(getJson()))) {
                    final JsonReader jsonReader = Json.createReader(r);
                    structure = jsonReader.read();
                }
                updateMessage("json loaded");
                updateProgress(2, 3);

                final MetaObject<JsonValue> metaObjects = processor.getMetaObjects();
                final Type jsonType = metaObjects.getMetaObject("JSON");
                final boolean result = metaObjects.is(structure, jsonType);
                updateMessage("end validation");
                updateProgress(3, 3);

                return result;
            }
        };
    }

    public final ObjectProperty<File> schemaProperty() {
        return this.schema;
    }

    public final File getSchema() {
        return this.schemaProperty().get();
    }

    public final void setSchema(final File schema) {
        this.schemaProperty().set(schema);
    }

    public final ObjectProperty<File> jsonProperty() {
        return this.json;
    }

    public final File getJson() {
        return this.jsonProperty().get();
    }

    public final void setJson(final File json) {
        this.jsonProperty().set(json);
    }

}
