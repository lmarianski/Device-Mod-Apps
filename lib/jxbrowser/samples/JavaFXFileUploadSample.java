/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.CloseStatus;
import com.teamdev.jxbrowser.chromium.FileChooserMode;
import com.teamdev.jxbrowser.chromium.FileChooserParams;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import com.teamdev.jxbrowser.chromium.javafx.DefaultDialogHandler;
import com.teamdev.jxbrowser.chromium.javafx.internal.FXUtil;
import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * The sample demonstrates how to register your DialogHandler and
 * override the functionality that displays file chooser when
 * user uploads file using INPUT TYPE="file" HTML element on a web page.
 */
public class JavaFXFileUploadSample extends Application {

    private static void displayFileChooserDialog(final FileChooserParams params,
            final AtomicReference<CloseStatus> result, final BrowserView view) {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Window window = FXUtil.getWindowForNode(view);
                    if (window != null) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open");
                        if (params.getMode() == FileChooserMode.Open) {
                            File file = fileChooser.showOpenDialog(window);
                            if (file != null) {
                                params.setSelectedFiles(file.getAbsolutePath());
                                result.set(CloseStatus.OK);
                            }
                        }

                        if (params.getMode() == FileChooserMode.OpenMultiple) {
                            List<File> files = fileChooser.showOpenMultipleDialog(window);
                            if (files != null) {
                                params.setSelectedFiles(files.toArray(new File[files.size()]));
                                result.set(CloseStatus.OK);
                            }
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }
        });
        try {
            latch.await();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

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
        final BrowserView view = new BrowserView(browser);
        browser.setDialogHandler(new DefaultDialogHandler(view) {
            @Override
            public CloseStatus onFileChooser(final FileChooserParams params) {
                AtomicReference<CloseStatus> result = new AtomicReference<CloseStatus>(
                        CloseStatus.CANCEL);
                displayFileChooserDialog(params, result, view);
                return result.get();
            }
        });

        Scene scene = new Scene(new BorderPane(view), 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        browser.loadURL("http://www.cs.tut.fi/~jkorpela/forms/file.html");
    }
}
