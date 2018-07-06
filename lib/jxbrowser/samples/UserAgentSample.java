/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to configure user-agent string
 * for all or specific Browser instances.
 */
public class UserAgentSample {
    public static void main(String[] args) {
        // The way to configure user-agent string for all Browser instances.
        // It's very important to configure user-agent string
        // before any Browser instance is created.
        BrowserPreferences.setUserAgent("My User Agent String");

        Browser browser1 = new Browser();
        BrowserView view1 = new BrowserView(browser1);

        JFrame frame1 = new JFrame();
        frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame1.add(view1, BorderLayout.CENTER);
        frame1.setSize(700, 500);
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
        browser1.setUserAgent("First User Agent String");
        browser1.loadURL("http://whatsmyuseragent.com/");

        // The way to configure user-agent string for specific Browser instance.
        Browser browser2 = new Browser();
        BrowserView view2 = new BrowserView(browser2);

        JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame2.add(view2, BorderLayout.CENTER);
        frame2.setSize(500, 300);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        browser2.setUserAgent("Second User Agent String");
        browser2.loadURL("http://whatsmyuseragent.com/");
    }
}
