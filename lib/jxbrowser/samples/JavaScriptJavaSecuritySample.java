/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSAccessible;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to inject Java object into JavaScript code and
 * tell JavaScript what public fields and methods JavaScript can access via the
 * JSAccessible annotation. See comments in the example.
 */
public class JavaScriptJavaSecuritySample {
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
        // the 'window.javaObject' property.
        JavaObject javaObject = new JavaObject();
        window.asObject().setProperty("javaObject", javaObject);

        // You can access public field marked with the JSAccessible annotation
        // of the injected Java object and modify their values.
        browser.executeJavaScriptAndReturnValue(
                "window.javaObject.accessibleField = 'Hello from JavaScript!';");

        // Access to public field that isn't marked with the JSAccessible
        // annotation will be denied and cause an error in JavaScript.
        browser.executeJavaScriptAndReturnValue(
                "window.javaObject.nonAccessibleField = 'Hello from JavaScript again!';");

        // Print a new value of the accessibleField.
        System.out.println("accessibleField = " + javaObject.accessibleField);
        System.out.println("nonAccessibleField = " + javaObject.nonAccessibleField);
    }

    /**
     * The class has two public fields. The 'accessibleField' public field marked
     * with the JSAccessible annotation. The 'nonAccessibleField' public field
     * isn't. If at least one public field of method is marked with the
     * JSAccessible annotation, it means that only public fields and methods with
     * this annotation can be accessed from JavaScript. Access to other public
     * fields or methods will cause an error in JavaScript. If class or its
     * superclass doesn't have public fields or methods marked with the
     * JSAccessible annotation, then it means that all public fields and methods
     * can be accessed from JavaScript.
     */
    public static class JavaObject {
        @JSAccessible
        public String accessibleField;
        public String nonAccessibleField;
    }
}
