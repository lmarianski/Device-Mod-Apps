/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to get a list of available frame IDs on
 * the loaded web page. Using frame ID parameter you can access document
 * frame to get its HTML, load specified URL in this frame or execute
 * JavaScript on it etc. In this sample we print HTML of each frame.
 */
public class GetFrameIDsSample {
    public static void main(String[] args) {
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
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                if (event.isMainFrame()) {
                    printFramesHierarchy(event.getBrowser(), Browser.MAIN_FRAME_ID);
                }
            }
        });
        browser.loadURL("http://docs.oracle.com/javase/8/docs/api/");
    }

    public static void printFramesHierarchy(Browser browser, long parentFrameId) {
        List<Long> childFrameIds = browser.getFramesIds(parentFrameId);
        for (Long childFrameId : childFrameIds) {
            System.out.println("Child Frame: " + childFrameId +
                    ", HTML: " + browser.getHTML(childFrameId));
            printFramesHierarchy(browser, childFrameId);
        }
    }
}
