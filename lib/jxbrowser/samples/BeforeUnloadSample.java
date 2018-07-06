/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.CloseStatus;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.UnloadDialogParams;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultDialogHandler;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to catch onbeforeunload dialog.
 */
public class BeforeUnloadSample {

    public static void main(String[] args) {
        LoggerProvider.setLevel(Level.FINE);
        final Browser browser = new Browser();
        final BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton dispose = new JButton("Require Dispose");
        dispose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("is disposed = " + browser.dispose(true));
                    }
                }).start();
            }
        });
        frame.add(dispose, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setDialogHandler(new DefaultDialogHandler(view) {
            @Override
            public CloseStatus onBeforeUnload(UnloadDialogParams params) {
                String title = "Confirm leaving this page";
                String message = params.getMessage();
                int returnValue = JOptionPane
                        .showConfirmDialog(view, message, title, JOptionPane.OK_CANCEL_OPTION);
                if (returnValue == JOptionPane.OK_OPTION) {
                    return CloseStatus.OK;
                } else {
                    return CloseStatus.CANCEL;
                }
            }
        });

        browser.loadHTML("<html><body onbeforeunload='return myFunction()'>" +
                "<a href='http://www.google.com'>Click here to leave</a>" +
                "<script>function myFunction() { return 'Leave this web page?'; }" +
                "</script></body></html>");
    }
}
