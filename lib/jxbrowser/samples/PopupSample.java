/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.PopupContainer;
import com.teamdev.jxbrowser.chromium.PopupHandler;
import com.teamdev.jxbrowser.chromium.PopupParams;
import com.teamdev.jxbrowser.chromium.events.DisposeEvent;
import com.teamdev.jxbrowser.chromium.events.DisposeListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to register popup handler and
 * implement functionality that displays popup windows.
 */
public class PopupSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        final BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setPopupHandler(new PopupHandler() {
            public PopupContainer handlePopup(PopupParams params) {
                return new PopupContainer() {
                    public void insertBrowser(final Browser browser,
                            final Rectangle initialBounds) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                BrowserView popupView = new BrowserView(browser);
                                popupView.setPreferredSize(initialBounds.getSize());

                                final JFrame popupFrame = new JFrame("Popup");
                                popupFrame
                                        .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                                popupFrame.add(popupView, BorderLayout.CENTER);
                                popupFrame.pack();
                                popupFrame.setLocation(initialBounds.getLocation());
                                popupFrame.setVisible(true);
                                popupFrame.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        browser.dispose();
                                    }
                                });

                                browser.addDisposeListener(new DisposeListener<Browser>() {
                                    public void onDisposed(DisposeEvent<Browser> event) {
                                        popupFrame.setVisible(false);
                                    }
                                });
                            }
                        });
                    }
                };
            }
        });

        browser.loadURL("http://www.popuptest.com");
    }
}
