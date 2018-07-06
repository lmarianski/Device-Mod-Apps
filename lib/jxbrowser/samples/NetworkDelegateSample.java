/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.BeforeSendHeadersParams;
import com.teamdev.jxbrowser.chromium.BeforeURLRequestParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.HttpHeadersEx;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to use request handler.
 */
public class NetworkDelegateSample {
    public static void main(String[] args) {
        LoggerProvider.setLevel(Level.OFF);

        BrowserContext browserContext = BrowserContext.defaultContext();
        browserContext.getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {
            @Override
            public void onBeforeURLRequest(BeforeURLRequestParams params) {
                // If navigate to teamdev.com, then change URL to google.com.
                if (params.getURL().equals("http://www.teamdev.com/")) {
                    params.setURL("www.google.com");
                }
            }

            @Override
            public void onBeforeSendHeaders(BeforeSendHeadersParams params) {
                // If navigate to google.com, then print User-Agent header value.
                if (params.getURL().equals("http://www.google.com/")) {
                    HttpHeadersEx headers = params.getHeadersEx();
                    System.out.println("User-Agent: " + headers.getHeader("User-Agent"));
                }
            }
        });

        Browser browser = new Browser(browserContext);
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.loadURL("http://www.teamdev.com/");
    }
}
