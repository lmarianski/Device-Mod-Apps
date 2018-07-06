/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.LoadHTMLParams;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.WebStorage;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to access WebStorage on
 * the loaded web page using JxBrowser API.
 */
public class WebStorageSample {
    public static void main(String[] args) {
        LoggerProvider.setLevel(Level.OFF);

        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onDocumentLoadedInMainFrame(LoadEvent event) {
                Browser browser = event.getBrowser();
                WebStorage webStorage = browser.getLocalWebStorage();
                // Read and display the 'myKey' storage value.
                System.out.println("The myKey value: " + webStorage.getItem("myKey"));
                // Modify the 'myKey' storage value.
                webStorage.setItem("myKey", "Hello from Local Storage");
            }
        });

        browser.loadHTML(new LoadHTMLParams(
                "<html><body><button onclick=\"myFunction()\">Modify 'myKey' value</button>" +
                        "<script>localStorage.myKey = \"Initial Value\";" +
                        "function myFunction(){alert(localStorage.myKey);}" +
                        "</script></body></html>",
                "UTF-8",
                "http://teamdev.com"));
    }
}
