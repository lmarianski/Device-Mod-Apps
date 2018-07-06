/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to disable functionality that allows
 * dragging/dropping content from/onto the loaded web page.
 * <p>
 * By default Drag&Drop is enabled. You can disable default behavior
 * using the "jxbrowser.chromium.dnd.enabled" System Property. For example:
 * System.setProperty("jxbrowser.chromium.dnd.enabled", "false");
 */
public class DisableDragAndDropSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        // Disable Drag and Drop.
        view.setDragAndDropEnabled(false);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.loadURL("http://www.google.com");
    }
}
