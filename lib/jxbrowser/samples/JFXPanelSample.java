/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The example demonstrates how to use JavaFX BrowserView in JFXPanel.
 */
public class JFXPanelSample {

    private static void initFX(JFXPanel fxPanel) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(view);
        Scene scene = new Scene(borderPane, 700, 500);
        fxPanel.setScene(scene);

        browser.loadURL("http://www.google.com");
    }

    public static void main(String[] args) {
        final JFXPanel fxPanel = new JFXPanel();

        JFrame frame = new JFrame("JFXPanel Sample");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(fxPanel, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
    }
}
