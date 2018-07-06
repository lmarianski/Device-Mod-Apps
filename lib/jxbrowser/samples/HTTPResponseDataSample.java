/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.DataReceivedParams;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.NetworkService;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;
import java.awt.BorderLayout;
import java.nio.charset.Charset;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to capture response body of HTTP request.
 */
public class HTTPResponseDataSample {
    public static void main(final String[] args) {
        LoggerProvider.setLevel(Level.OFF);

        BrowserContext browserContext = BrowserContext.defaultContext();
        NetworkService networkService = browserContext.getNetworkService();
        networkService.setNetworkDelegate(new DefaultNetworkDelegate() {
            @Override
            public void onDataReceived(DataReceivedParams params) {
                if (params.getMimeType().equals("text/html")) {
                    String data = new String(params.getData(),
                            Charset.forName("UTF-8"));
                    System.out.println("data = " + data);
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

        browser.loadURL("https://www.wikipedia.org/");
    }
}
