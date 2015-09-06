// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.tasogare.json.ds.app.analyzer.i18n.Messages;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * XXX: 開発中は以下の引数を渡して実行しているが何か操作をするとどれかが悪さをしてハングアップするがどれか特定していない
 * <pre>
 * -Djavafx.verbose=true
 * -Djavafx.debug=false
 * -Djavafx.pulseLogger=true
 * -Dquantum.verbose=true
 * -Dquantum.debug=true
 * -Dquantum.pulse=true
 * -Dcom.sun.scenario.animation.adaptivepulse=true
 * -Dcom.sun.javafx.highContrastTheme=BLACKONWHITE
 * </pre>
 * @author tasogare
 *
 */
public class App extends Application {

    private final ResourceBundle RESOURCES;
    public App() {
        RESOURCES = ResourceBundle.getBundle(getClass().getPackage().getName() + ".resources.App");
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final URL fxml = getClass().getResource("App.fxml");
        final FXMLLoader loader = new FXMLLoader(fxml, RESOURCES);
        final BorderPane root = loader.load();

        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            final AppController ctor = loader.getController();
            if (newValue.booleanValue()) {
                // 全画面ならトグルボタンのテキストをFullscreenButtonOnにして選択済みにする
                ctor.fullscreenToggleButton.setText(Messages.FullscreenButtonOn.from(RESOURCES));
                ctor.fullscreenToggleButton.setSelected(true);
                // その後位置とサイズを変更
                final Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());
            } else {
                // 全画面ではないならトグルボタンのテキストをFullscreenButtonOffにして選択解除する
                ctor.fullscreenToggleButton.setText(Messages.FullscreenButtonOff.from(RESOURCES));
                ctor.fullscreenToggleButton.setSelected(false);
                // その後位置とサイズを変更
                primaryStage.sizeToScene();
                primaryStage.centerOnScreen();
            }
        });

        primaryStage.setTitle(Messages.AppTitle.from(RESOURCES));
        primaryStage.getIcons().add(new Image(getClass().getResource("./resources/icon.png").toExternalForm()));
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String... args) {
        Application.launch(App.class, args);
    }
}
