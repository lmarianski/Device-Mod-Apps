/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.AuthRequiredParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.CustomProxyConfig;
import com.teamdev.jxbrowser.chromium.internal.FileUtil;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;

/**
 * The sample demonstrates how to configure Browser instance to use custom proxy settings.
 * By default Browser instance uses system proxy settings.
 */
public class ProxySample {
    public static void main(String[] args) {
        String dataDir = FileUtil.createTempDir("chromium-data").getAbsolutePath();
        BrowserContextParams contextParams = new BrowserContextParams(dataDir);

        // Browser will automatically detect proxy settings.
        // contextParams.setProxyConfig(new AutoDetectProxyConfig());

        // Browser will not use a proxy server.
        // contextParams.setProxyConfig(new DirectProxyConfig());

        // Browser will use proxy settings received from proxy auto-config (PAC) file.
        // contextParams.setProxyConfig(new URLProxyConfig("<pac-file-url>"));

        // Browser will use custom user's proxy settings.
        String proxyRules = "http=foo:80;https=foo:80;ftp=foo:80;socks=foo:80";
        String exceptions = "<local>";  // bypass proxy server for local web pages
        contextParams.setProxyConfig(new CustomProxyConfig(proxyRules, exceptions));

        // Creates Browser instance with context configured to use specified proxy settings.
        Browser browser = new Browser(new BrowserContext(contextParams));
        // Handle proxy authorization.
        browser.getContext().getNetworkService().setNetworkDelegate(new DefaultNetworkDelegate() {
            @Override
            public boolean onAuthRequired(AuthRequiredParams params) {
                // If proxy server requires login/password, provide it programmatically.
                if (params.isProxy()) {
                    params.setUsername("proxy-username");
                    params.setPassword("proxy-password");
                    return false;
                }
                return true;
            }
        });
    }
}