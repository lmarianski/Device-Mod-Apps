/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Cookie;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to suppress/filter incoming and outgoing cookies.
 */
public class CookieFilterSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        // Suppress/filter all incoming and outgoing cookies.
        browser.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {
            @Override
            public boolean onCanSetCookies(String url, List<Cookie> cookies) {
                return false;
            }

            @Override
            public boolean onCanGetCookies(String url, List<Cookie> cookies) {
                return false;
            }
        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
