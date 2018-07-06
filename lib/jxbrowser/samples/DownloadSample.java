/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.events.DownloadEvent;
import com.teamdev.jxbrowser.chromium.events.DownloadListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to handle file download. To cancel download
 * you must return {@code false} from the
 * {@link DownloadHandler#allowDownload(com.teamdev.jxbrowser.chromium.DownloadItem)}
 * method. To listed for download update events you can register your own
 * {@link com.teamdev.jxbrowser.chromium.events.DownloadListener}.
 */
public class DownloadSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setDownloadHandler(new DownloadHandler() {
            public boolean allowDownload(DownloadItem download) {
                download.addDownloadListener(new DownloadListener() {
                    public void onDownloadUpdated(DownloadEvent event) {
                        DownloadItem download = event.getDownloadItem();
                        if (download.isCompleted()) {
                            System.out.println("Download is completed!");
                        }
                    }
                });
                System.out.println("Destination file: " +
                        download.getDestinationFile().getAbsolutePath());
                return true;
            }
        });

        browser.loadURL("ftp://ftp.teamdev.com/updates/jxbrowser-4.0-beta.zip");
    }
}
