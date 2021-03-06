// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer;

import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.JsonChooserExtFilterAllDesc;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.JsonChooserExtFilterJsonDesc;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.JsonChooserTitle;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.LoadServiceFailedMessage;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.ValidateServiceFailedMessage;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.ValidateServiceSucceededMessage;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.jsdsChooserExtFilterAllDesc;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.jsdsChooserExtFilterJsdsDesc;
import static com.github.tasogare.json.ds.app.analyzer.i18n.Messages.jsdsChooserTitle;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.app.analyzer.model.SourcePear;
import com.github.tasogare.json.ds.app.analyzer.model.service.JsonDsLoadService;
import com.github.tasogare.json.ds.app.analyzer.model.service.JsonValidateService;
import com.github.tasogare.json.ds.parser.SourceInfo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AppController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Button analyzeButton;

    @FXML
    private Button validateButton;

    @FXML
    ToggleButton fullscreenToggleButton;

    private final SourcePear model = new SourcePear();

    @FXML
    private void initialize() {
        assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'App.fxml'.";
        assert analyzeButton != null : "fx:id=\"analyzeButton\" was not injected: check your FXML file 'App.fxml'.";
        assert validateButton != null : "fx:id=\"validateButton\" was not injected: check your FXML file 'App.fxml'.";
        assert fullscreenToggleButton != null : "fx:id=\"fullscreenToggleButton\" was not injected: check your FXML file 'App.fxml'.";
        validateButton.disableProperty().bind(getModel().schemaProperty().isNull());
    }

    @FXML
    private void handleAnalyzeButton(final ActionEvent evt) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(jsdsChooserTitle.from(resources));
        fileChooser.getExtensionFilters()
            .addAll(new ExtensionFilter(jsdsChooserExtFilterJsdsDesc.from(resources), "*.jsds"),
                    new ExtensionFilter(jsdsChooserExtFilterAllDesc.from(resources), "*.*"));
        final File selectedFile = fileChooser.showOpenDialog(treeView.getScene().getWindow());
        getModel().setJson(null);
        if (selectedFile != null) {
            final JsonDsLoadService jsdsLoadService = new JsonDsLoadService(selectedFile);
            jsdsLoadService.setOnSucceeded(wse -> {
                getModel().setSchema(selectedFile);
                final TreeItem<String> value = jsdsLoadService.getValue();
                value.setExpanded(true);
                treeView.setRoot(value);
            });
            jsdsLoadService.setOnFailed(wse -> {
                getModel().setSchema(null);
                showErrorInDetail(wse.getSource().getException(), LoadServiceFailedMessage.from(resources));
            });
            jsdsLoadService.progressProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println(jsdsLoadService.getMessage());
                System.out.println(newValue);
            });
            jsdsLoadService.start();
        }
    }

    @FXML
    private void handleValidateButton(final ActionEvent evt) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(JsonChooserTitle.from(resources));
        fileChooser.getExtensionFilters()
            .addAll(new ExtensionFilter(JsonChooserExtFilterJsonDesc.from(resources), "*.json"),
                    new ExtensionFilter(JsonChooserExtFilterAllDesc.from(resources), "*.*"));
        final File selectedFile = fileChooser.showOpenDialog(treeView.getScene().getWindow());
        if (selectedFile != null) {
            getModel().setJson(selectedFile);
            final JsonValidateService validateService = new JsonValidateService(getModel().getSchema(), selectedFile);
            validateService.setOnSucceeded(wse -> {
                final boolean valid = (Boolean) wse.getSource().getValue();
                final String mess = ValidateServiceSucceededMessage.format(resources, getModel().getJson().getName(),
                                                                           valid ? "valid" : "invalid");
                Platform.runLater(() -> {
                    final Alert alert = new Alert(valid ? AlertType.CONFIRMATION : AlertType.WARNING);
                    alert.initOwner(treeView.getScene().getWindow());
                    alert.setContentText(mess);
                    alert.show();
                });
            });
            validateService.setOnFailed(wse -> {
                showErrorInDetail(wse.getSource().getException(), ValidateServiceFailedMessage.from(resources));
            });
            validateService.progressProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println(validateService.getMessage());
                System.out.println(newValue);
            });
            validateService.start();
        }
    }

    private void showErrorInDetail(Throwable exception, String headerText) {
        final StringWriter message = new StringWriter();
        
        try (final PrintWriter writer = new PrintWriter(message, true)) {
            if (exception instanceof StaticSemanticsException) {
                reportError((StaticSemanticsException) exception, writer);
            } else {
                exception.printStackTrace(writer);
            }
        }
        Platform.runLater(() -> {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(treeView.getScene().getWindow());
            alert.setHeaderText(headerText);
            final TextArea content = new TextArea(message.toString());
            //FIXME: javafxに論理フォントを指定する術がない
            final Font monospace = new Font("Consolas", 12);
            content.setFont(monospace);
            content.setEditable(false);
            alert.getDialogPane().setExpandableContent(content);
            alert.show();
        });
    }

    private static void reportError(StaticSemanticsException e, PrintWriter writer) {
        final SourceInfo info = e.getSourceInfo();
        writer.println(info.getSourceName());
        writer.println("row: " + info.getRow() + " col: " + info.getColumn());
        writer.println(info.getSource().renge(info.getLineStart(), info.getPosition()));

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < info.getColumn() - 1; i++) {
            sb.append(".");
        }
        writer.println(sb.append("^").toString());
        sb.delete(0, sb.length());
        sb.append(e.getErrorType().toString()).append(": ").append(e.getMessage());
        writer.println(sb.toString());
//        e.printStackTrace();
    }

    @FXML
    private void handleFullscreenToggleButton(final ActionEvent evt) {
        final Stage primaryStage = (Stage) treeView.getScene().getWindow();
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }

    /**
     * @return the model
     */
    private final SourcePear getModel() {
        return model;
    }
}
