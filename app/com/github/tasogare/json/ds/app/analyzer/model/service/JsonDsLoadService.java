// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.github.tasogare.json.ds.app.analyzer.model.TreeItemMaker;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class JsonDsLoadService extends Service<TreeItem<String>> {

    /**
     * 読み込む対象のスキーマファイル
     */
    private final SimpleObjectProperty<File> schema;

    public JsonDsLoadService(final File schema) {
        this.schema = new SimpleObjectProperty<>(this, "schema", schema);
    }

    @Override
    protected Task<TreeItem<String>> createTask() {
        return new Task<TreeItem<String>>() {
            @Override
            protected TreeItem<String> call() throws Exception {
                try (final BufferedReader r = new BufferedReader(new FileReader(getSchema()))) {
                    final Source source = new Source(r);
                    final TokenStream ts = new TokenStream(source);
                    final Parser parser = new Parser(ts, getSchema().toURI().toString());
                    final ProgramNode<?> p = parser.parse();
                    final TreeItemMaker maker = new TreeItemMaker(getSchema().getAbsoluteFile().toURI().toString());
                    return maker.visit(p);
                }
            }
        };
    }

    public final SimpleObjectProperty<File> schemaProperty() {
        return schema;
    }

    public final File getSchema() {
        return schemaProperty().get();
    }

    public final void setSchema(final File schema) {
        schemaProperty().set(schema);
    }
}
