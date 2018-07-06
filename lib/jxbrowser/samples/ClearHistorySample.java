/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Callback;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to clear entire navigation history.
 */
public class ClearHistorySample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadURLAndWaitReady(browser, "http://www.google.com");
        loadURLAndWaitReady(browser, "http://www.teamdev.com");

        // Returns the number of entries in the back/forward list.
        int entryCount = browser.getNavigationEntryCount();
        // Remove navigation entries at index.
        for (int i = entryCount - 2; i >= 0; i--) {
            boolean success = browser.removeNavigationEntryAtIndex(i);
            System.out.println("Navigation entry at index " + i +
                    " has been removed successfully? " + success);
        }
    }

    private static void loadURLAndWaitReady(Browser browser, final String url) {
        Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
            @Override
            public void invoke(Browser value) {
                value.loadURL(url);
            }
        });
    }
}
