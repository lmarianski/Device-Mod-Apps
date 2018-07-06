/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.NetworkService;
import com.teamdev.jxbrowser.chromium.ResourceHandler;
import com.teamdev.jxbrowser.chromium.ResourceParams;
import com.teamdev.jxbrowser.chromium.ResourceType;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to handle all resources such as
 * HTML, PNG, JavaScript, CSS files and decide whether web browser
 * engine should load them from web server or not. For example, in
 * this sample we cancel loading of all Images.
 */
public class ResourceHandlerSample {
    public static void main(String[] args) {
        LoggerProvider.setLevel(Level.OFF);

        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        BrowserContext context = browser.getContext();
        NetworkService networkService = context.getNetworkService();
        networkService.setResourceHandler(new ResourceHandler() {
            @Override
            public boolean canLoadResource(ResourceParams params) {
                System.out.println("URL: " + params.getURL());
                System.out.println("Type: " + params.getResourceType());
                // Cancel loading of all images
                return params.getResourceType() != ResourceType.IMAGE;
            }
        });

        browser.loadURL("http://www.google.com");
    }
}
