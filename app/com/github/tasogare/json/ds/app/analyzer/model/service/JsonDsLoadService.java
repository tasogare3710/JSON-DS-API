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

import javafx.beans.property.ObjectProperty;
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
                    final String name = getSchema().getAbsoluteFile().toURI().toString();
                    final TokenStream ts = new TokenStream(new Source(r), name);
                    updateMessage("schema loaded");
                    updateProgress(1, 3);

                    final Parser parser = new Parser(ts);
                    final ProgramNode<?> p = parser.parse();
                    updateMessage("end parse");
                    updateProgress(2, 3);

                    final TreeItemMaker maker = new TreeItemMaker(name);
                    final TreeItem<String> treeRoot = maker.visit(p);
                    updateMessage("tree created");
                    updateProgress(3, 3);

                    return treeRoot;
                }
            }
        };
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
}
