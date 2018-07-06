/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.chromium.demo;

import com.teamdev.jxbrowser.chromium.demo.resources.Resources;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * @author TeamDev Ltd.
 */
public class JxBrowserDemo {

    private static void initEnvironment() throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "JxBrowser Demo");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }

    public static void main(String[] args) throws Exception {
        initEnvironment();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initAndDisplayUI();
            }
        });
    }

    private static void initAndDisplayUI() {
        final TabbedPane tabbedPane = new TabbedPane();
        insertTab(tabbedPane, TabFactory.createFirstTab());
        insertNewTabButton(tabbedPane);

        JFrame frame = new JFrame("JxBrowser Demo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @SuppressWarnings("CallToSystemExit")
            @Override
            public void windowClosing(WindowEvent e) {
                tabbedPane.disposeAllTabs();
                if (Environment.isMac()) {
                    System.exit(0);
                }
            }
        });
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setSize(1024, 700);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(Resources.getIcon("jxbrowser16x16.png").getImage());
        frame.setVisible(true);
    }

    private static void insertNewTabButton(final TabbedPane tabbedPane) {
        TabButton button = new TabButton(Resources.getIcon("new-tab.png"), "New tab");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                insertTab(tabbedPane, TabFactory.createTab());
            }
        });
        tabbedPane.addTabButton(button);
    }

    private static void insertTab(TabbedPane tabbedPane, Tab tab) {
        tabbedPane.addTab(tab);
        tabbedPane.selectTab(tab);
    }
}
