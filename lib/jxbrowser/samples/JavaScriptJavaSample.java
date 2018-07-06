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
public class JavaScriptJavaSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JSValue window = browser.executeJavaScriptAndReturnValue("window");

        // Inject Java object into JavaScript and associate it with
        // the window.javaObject property.
        JavaObject javaObject = new JavaObject();
        window.asObject().setProperty("javaObject", javaObject);

        // You can access public fields of the injected Java object
        // and modify their values. For example, we modify the title
        // field of the Java object directly from JavaScript.
        browser.executeJavaScriptAndReturnValue("window.javaObject.title = 'Hello Java';");

        // Print a new value of the field.
        System.out.println(javaObject.title);
    }

    public static class JavaObject {
        public String title;
    }
}
