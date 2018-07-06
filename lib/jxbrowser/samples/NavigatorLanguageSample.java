/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.internal.FileUtil;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultPopupHandler;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to change navigate.language JS property value using
 * Chromium --lang switcher.
 */
public class NavigatorLanguageSample {
    public static void main(String[] args) {
        BrowserPreferences.setChromiumSwitches("--lang=IT");

        String dataDir = FileUtil.createTempDir("tempDataDir").getAbsolutePath();
        BrowserContextParams params = new BrowserContextParams(dataDir, "IT");
        Browser browser = new Browser(new BrowserContext(params));
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setPopupHandler(new DefaultPopupHandler());
        browser.loadHTML("<!DOCTYPE html><html><body><p id=\"demo\"></p>\n" +
                "<script>\n" +
                "    var x = \"Language of the browser: \" + navigator.language;\n" +
                "    document.getElementById(\"demo\").innerHTML = x;\n" +
                "</script></body></html>");
    }
}