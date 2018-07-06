/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import com.teamdev.jxbrowser.chromium.javafx.internal.LightWeightWidget;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The sample demonstrates how to disable functionality that allows dragging
 * links from the loaded web page.
 */
public class JavaFXDisableDnDSample extends Application {

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
    public void start(final Stage primaryStage) {
        Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
        final BrowserView view = new BrowserView(browser);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                LightWeightWidget lightWeightWidget = (LightWeightWidget)
                        view.getChildren().get(0);
                if (lightWeightWidget.isDragAndDropEnabled()) {
                    // Now you cannot drag and drop links from the loaded web page.
                    lightWeightWidget.setDragAndDropEnabled(false);
                }
            }
        });

        Scene scene = new Scene(new BorderPane(view), 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        browser.loadURL("http://www.google.com");
    }
}