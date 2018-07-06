/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to register custom ContextMenuHandler,
 * to handle mouse right clicks and display custom Swing context menu.
 */
public class ContextMenuSample {
    public static void main(String[] args) {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setContextMenuHandler(new MyContextMenuHandler(view));
        browser.loadURL("http://www.google.com");
    }

    private static class MyContextMenuHandler implements ContextMenuHandler {

        private final JComponent component;

        private MyContextMenuHandler(JComponent parentComponent) {
            this.component = parentComponent;
        }

        private static JMenuItem createMenuItem(String title, final Runnable action) {
            JMenuItem reloadMenuItem = new JMenuItem(title);
            reloadMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action.run();
                }
            });
            return reloadMenuItem;
        }

        public void showContextMenu(final ContextMenuParams params) {
            final JPopupMenu popupMenu = new JPopupMenu();
            if (!params.getLinkText().isEmpty()) {
                popupMenu.add(createMenuItem("Open link in new window", new Runnable() {
                    public void run() {
                        String linkURL = params.getLinkURL();
                        System.out.println("linkURL = " + linkURL);
                    }
                }));
            }

            final Browser browser = params.getBrowser();
            popupMenu.add(createMenuItem("Reload", new Runnable() {
                public void run() {
                    browser.reload();
                }
            }));

            final Point location = params.getLocation();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    popupMenu.show(component, location.x, location.y);
                }
            });
        }
    }
}
