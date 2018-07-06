/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.FullScreenHandler;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Demonstrates how to switch between full screen mode and window mode.
 */
public class JavaFXFullScreenSample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
        if (Environment.isMac()) {
            BrowserCore.initialize();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        BorderPane border = new BorderPane(view);
        Scene scene = new Scene(border, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        browser.setFullScreenHandler(new MyFullScreenHandler(primaryStage, border, view));
        browser.loadURL("http://www.quirksmode.org/html5/tests/video.html");
    }

    private static class MyFullScreenHandler implements FullScreenHandler {
        private final Stage parentStage;
        private final Pane parentPane;
        private final BrowserView view;
        private final Stage stage;
        private final BorderPane pane;

        private MyFullScreenHandler(Stage parentStage, Pane parentPane, BrowserView view) {
            this.parentStage = parentStage;
            this.parentPane = parentPane;
            this.view = view;
            this.pane = new BorderPane();

            Scene scene = new Scene(pane);
            this.stage = new Stage();
            this.stage.setScene(scene);
        }

        @Override
        public void onFullScreenEnter() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.setCenter(view);
                    stage.setFullScreen(true);
                    stage.show();
                    parentStage.hide();
                }
            });
        }

        @Override
        public void onFullScreenExit() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    parentPane.getChildren().add(view);
                    parentStage.show();
                    stage.hide();
                }
            });
        }
    }
}
