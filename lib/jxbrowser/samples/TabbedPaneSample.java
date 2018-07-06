/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to use Browser in JTabbedPane.
 */
public class TabbedPaneSample {
    public static void main(String[] args) {
        Browser browserOne = new Browser();
        Browser browserTwo = new Browser();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Browser One", new BrowserView(browserOne));
        tabbedPane.addTab("Browser Two", new BrowserView(browserTwo));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browserOne.loadURL("http://www.google.com");
        browserTwo.loadURL("http://www.teamdev.com");
    }
}
