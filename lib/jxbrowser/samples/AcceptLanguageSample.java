/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.BeforeSendHeadersParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.Callback;
import com.teamdev.jxbrowser.chromium.internal.FileUtil;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to set up the Accept-Language for browser instance.
 */
public class AcceptLanguageSample {
    public static void main(String[] args) {
        String dataDir = FileUtil.createTempDir("tempDataDir").getAbsolutePath();
        BrowserContextParams params = new BrowserContextParams(dataDir, "IT");
        final Browser browser = new Browser(new BrowserContext(params));
        BrowserView view = new BrowserView(browser);

        browser.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {

            @Override
            public void onBeforeSendHeaders(BeforeSendHeadersParams params) {
                System.out.println(
                        "Accept-Language = " + params.getHeadersEx().getHeader("Accept-Language"));
            }

        });

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
            @Override
            public void invoke(Browser value) {
                browser.loadURL("google.com");
            }
        });
        browser.setAcceptLanguage("FR");
        browser.reload();
    }
}
