/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The sample demonstrates how to use JavaFX BrowserView in TabPane.
 */
public class JavaFXTabPaneSample extends Application {

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
        // Tab One
        Browser browserOne = new Browser();
        browserOne.loadURL("http://www.google.com");
        BrowserView viewOne = new BrowserView(browserOne);

        Tab tabOne = new Tab("Browser One");
        tabOne.setContent(viewOne);

        // Tab Two
        Browser browserTwo = new Browser();
        browserTwo.loadURL("http://www.teamdev.com");
        BrowserView viewTwo = new BrowserView(browserTwo);

        Tab tabTwo = new Tab("Browser Two");
        tabTwo.setContent(viewTwo);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tabOne);
        tabPane.getTabs().add(tabTwo);

        Group root = new Group();
        Scene scene = new Scene(root, 700, 500);

        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(tabPane);

        root.getChildren().add(borderPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
