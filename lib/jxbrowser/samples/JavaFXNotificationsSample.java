/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.Notification;
import com.teamdev.jxbrowser.chromium.NotificationHandler;
import com.teamdev.jxbrowser.chromium.NotificationService;
import com.teamdev.jxbrowser.chromium.PermissionHandler;
import com.teamdev.jxbrowser.chromium.PermissionRequest;
import com.teamdev.jxbrowser.chromium.PermissionStatus;
import com.teamdev.jxbrowser.chromium.PermissionType;
import com.teamdev.jxbrowser.chromium.events.NotificationEvent;
import com.teamdev.jxbrowser.chromium.events.NotificationListener;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Demonstrates how to handle HTML5 Desktop Notifications in JavaFX Application.
 */
public class JavaFXNotificationsSample extends Application {

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
        final Browser browser = new Browser();
        final BrowserView view = new BrowserView(browser);

        BrowserContext context = browser.getContext();
        NotificationService notificationService = context.getNotificationService();
        notificationService.setNotificationHandler(new NotificationHandler() {
            @Override
            public void onShowNotification(NotificationEvent event) {
                final Notification notification = event.getNotification();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        final Stage stage = new Stage();
                        stage.setTitle(notification.getTitle());
                        Label label = new Label(notification.getMessage());
                        label.setPadding(new Insets(15, 15, 15, 15));
                        stage.setScene(new Scene(label, 300, 100));
                        stage.setAlwaysOnTop(true);
                        stage.show();

                        notification.addNotificationListener(new NotificationListener() {
                            @Override
                            public void onClose(NotificationEvent event) {
                                if (event.getNotification().isClosed()) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            stage.close();
                                        }
                                    });
                                }
                            }
                        });

                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                if (event.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
                                    event.consume();
                                    notification.close();
                                }
                            }
                        });
                    }
                });
            }
        });

        // Grant notification permission if it's necessary
        browser.setPermissionHandler(new PermissionHandler() {
            @Override
            public PermissionStatus onRequestPermission(PermissionRequest request) {
                if (request.getPermissionType() == PermissionType.NOTIFICATIONS) {
                    return PermissionStatus.GRANTED;
                }
                return PermissionStatus.DENIED;
            }
        });

        Scene scene = new Scene(new BorderPane(view), 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        browser.loadURL("notifications.html");
    }
}
