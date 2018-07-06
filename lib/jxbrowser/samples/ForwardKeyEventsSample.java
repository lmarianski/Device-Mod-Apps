/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_A;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_E;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_H;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_L;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_O;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode.VK_RETURN;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyEventType.PRESSED;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyEventType.RELEASED;
import static com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyEventType.TYPED;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyCode;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiers;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiersBuilder;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to create and forward keyboard events
 * containing characters, modifiers, and control keys to Chromium engine.
 */
public class ForwardKeyEventsSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    Browser browser = event.getBrowser();

                    // Sending 'Hello' to the currently focused textarea
                    forwardKeyEvent(browser, VK_H, 'H');
                    forwardKeyEvent(browser, VK_E, 'e');
                    forwardKeyEvent(browser, VK_L, 'l');
                    forwardKeyEvent(browser, VK_L, 'l');
                    forwardKeyEvent(browser, VK_O, 'o');

                    // Sending 'Enter' to insert a line break
                    forwardKeyEvent(browser, VK_RETURN);

                    // Selecting text in the textarea using Ctrl+A shortcut
                    forwardKeyEvent(browser, VK_A,
                            new KeyModifiersBuilder().ctrlDown().build());
                }
            }
        });

        browser.loadHTML("<textarea autofocus rows='10' cols='30'></textarea>");
    }

    private static void forwardKeyEvent(Browser browser, KeyCode code, char character) {
        browser.forwardKeyEvent(new BrowserKeyEvent(PRESSED, code, character));
        browser.forwardKeyEvent(new BrowserKeyEvent(TYPED, code, character));
        browser.forwardKeyEvent(new BrowserKeyEvent(RELEASED, code, character));
    }

    private static void forwardKeyEvent(Browser browser, KeyCode code) {
        browser.forwardKeyEvent(new BrowserKeyEvent(PRESSED, code));
        browser.forwardKeyEvent(new BrowserKeyEvent(TYPED, code));
        browser.forwardKeyEvent(new BrowserKeyEvent(RELEASED, code));
    }

    private static void forwardKeyEvent(Browser browser, KeyCode code, KeyModifiers modifiers) {
        browser.forwardKeyEvent(new BrowserKeyEvent(PRESSED, code, modifiers));
        browser.forwardKeyEvent(new BrowserKeyEvent(TYPED, code, modifiers));
        browser.forwardKeyEvent(new BrowserKeyEvent(RELEASED, code, modifiers));
    }
}
