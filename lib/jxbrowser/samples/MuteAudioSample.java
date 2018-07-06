/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to mute audio sound on the opened web page
 * and check whether audio is muted or not.
 */
public class MuteAudioSample {
    public static void main(String[] args) {
        final Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        final JButton muteAudioButton = new JButton();
        muteAudioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser.setAudioMuted(!browser.isAudioMuted());
                updateButtonText(muteAudioButton, browser);
            }
        });
        updateButtonText(muteAudioButton, browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(muteAudioButton, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.loadURL("https://www.youtube.com/");
    }

    private static void updateButtonText(final JButton button, final Browser browser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                button.setText(browser.isAudioMuted() ? "Unmute Audio" : "Mute Audio");
            }
        });
    }
}
