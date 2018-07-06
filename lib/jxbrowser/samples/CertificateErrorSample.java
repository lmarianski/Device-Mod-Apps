/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Certificate;
import com.teamdev.jxbrowser.chromium.CertificateErrorParams;
import com.teamdev.jxbrowser.chromium.DefaultLoadHandler;
import com.teamdev.jxbrowser.chromium.Principal;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Demonstrates how to handle SSL certificate errors.
 */
public class CertificateErrorSample {
    public static void main(String[] args) {
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setLoadHandler(new DefaultLoadHandler() {
            @Override
            public boolean onCertificateError(CertificateErrorParams params) {
                Certificate certificate = params.getCertificate();

                System.out.println("ErrorCode = " + params.getCertificateError());
                System.out.println("SerialNumber = " + certificate.getSerialNumber());
                System.out.println("FingerPrint = " + certificate.getFingerPrint());
                System.out.println("CAFingerPrint = " + certificate.getCAFingerPrint());

                Principal subject = certificate.getSubject();
                System.out.println("Subject CommonName = " + subject.getCommonName());
                System.out.println("Subject LocalityName = " + subject.getLocalityName());
                System.out.println(
                        "Subject StateOrProvinceName = " + subject.getStateOrProvinceName());
                System.out.println("Subject CountryName = " + subject.getCountryName());
                System.out.println("Subject StreetAddress = " + subject.getStreetAddress());
                System.out.println("Subject OrganizationName = " + subject.getOrganizationName());
                System.out.println(
                        "Subject OrganizationUnitName = " + subject.getOrganizationUnitName());
                System.out.println("Subject DomainComponent = " + subject.getDomainComponent());

                Principal issuer = certificate.getIssuer();
                System.out.println("Issuer CommonName = " + issuer.getCommonName());
                System.out.println("Issuer LocalityName = " + issuer.getLocalityName());
                System.out
                        .println("Issuer StateOrProvinceName = " + issuer.getStateOrProvinceName());
                System.out.println("Issuer CountryName = " + issuer.getCountryName());
                System.out.println("Issuer StreetAddress = " + issuer.getStreetAddress());
                System.out.println("Issuer OrganizationName = " + issuer.getOrganizationName());
                System.out.println(
                        "Issuer OrganizationUnitName = " + issuer.getOrganizationUnitName());
                System.out.println("Issuer DomainComponent = " + issuer.getDomainComponent());

                System.out.println(
                        "SubjectAlternativeNames = " + certificate.getSubjectAlternativeNames());
                System.out.println("KeyUsages = " + certificate.getKeyUsages());
                System.out.println("ExtendedKeyUsages = " + certificate.getExtendedKeyUsages());

                System.out.println("HasExpired = " + certificate.hasExpired());

                // Return false to ignore certificate error.
                return false;
            }
        });
        browser.loadURL("<https-url-with-invalid-ssl-certificate>");
    }
}
