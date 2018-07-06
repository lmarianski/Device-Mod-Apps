/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to create Browser instance with transparent background.
 */
public class TransparentPageSample {
    public static void main(String[] args) {
        Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserPreferences prefs = browser.getPreferences();
        prefs.setTransparentBackground(true);
        browser.setPreferences(prefs);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 150, 255, 255));
        panel.add(new BrowserView(browser), BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.loadHTML("<html><body>"
                + "<div style='background: yellow; opacity: 0.7;'>\n"
                + "    This text is in the yellow half-transparent div and should "
                + "    appear as in the green due to the blue JPanel behind."
                + "</div>\n"
                + "<div style='background: red;'>\n"
                + "    This text is in the red opaque div and should appear as is."
                + "</div>\n"
                + "<div>\n"
                + "    This text is in the non-styled div and should appear as in "
                + "    the blue due to the blue JPanel behind."
                + "</div>\n"
                + "</body></html>");
    }
}