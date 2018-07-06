/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The sample demonstrates how to embed JavaFX BrowserView
 * component into JavaFX app using FXML.
 */
public class FXMLSample extends Application {

    public static void main(String[] args) {
        Application.launch(FXMLSample.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane pane = FXMLLoader.load(
                FXMLSample.class.getResource("browser-view-control.fxml"));

        primaryStage.setTitle("FXMLSample");
        primaryStage.setScene(new Scene(pane, 800, 600));
        primaryStage.show();
    }
}
