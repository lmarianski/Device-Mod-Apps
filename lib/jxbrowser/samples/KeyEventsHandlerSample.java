/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.InputEventsHandler;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to register key events handler to handle/suppress
 * Ctrl+A key events (e.g. to prevent text selection).
 */
public class KeyEventsHandlerSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        view.setKeyEventsHandler(new InputEventsHandler<KeyEvent>() {
            public boolean handle(KeyEvent event) {
                return event.isControlDown() && event.getKeyCode() == KeyEvent.VK_A;
            }
        });

        browser.loadURL("http://www.google.com");
    }
}
