/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to work with JavaScript objects from Java code.
 */
public class JavaScriptObjectsSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JSValue document = browser.executeJavaScriptAndReturnValue("document");
        if (document.isObject()) {
            // document.title = "New Title"
            document.asObject().setProperty("title", "New Title");

            // document.write("Hello World!")
            JSValue write = document.asObject().getProperty("write");
            if (write.isFunction()) {
                write.asFunction().invoke(document.asObject(), "Hello World!");
            }
        }
    }
}
