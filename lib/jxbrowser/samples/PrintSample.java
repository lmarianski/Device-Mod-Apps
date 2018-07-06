/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ColorModel;
import com.teamdev.jxbrowser.chromium.DuplexMode;
import com.teamdev.jxbrowser.chromium.PageRange;
import com.teamdev.jxbrowser.chromium.PaperSize;
import com.teamdev.jxbrowser.chromium.PrintHandler;
import com.teamdev.jxbrowser.chromium.PrintJob;
import com.teamdev.jxbrowser.chromium.PrintSettings;
import com.teamdev.jxbrowser.chromium.PrintStatus;
import com.teamdev.jxbrowser.chromium.events.PrintJobEvent;
import com.teamdev.jxbrowser.chromium.events.PrintJobListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to print currently loaded web page with custom print settings.
 */
public class PrintSample {
    public static void main(String[] args) {
        final Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton print = new JButton("Print");
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser.print();
            }
        });
        frame.add(print, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        browser.setPrintHandler(new PrintHandler() {
            @Override
            public PrintStatus onPrint(PrintJob printJob) {
                PrintSettings printSettings = printJob.getPrintSettings();
                printSettings.setPrinterName("Microsoft XPS Document Writer");
                printSettings.setLandscape(false);
                printSettings.setPrintBackgrounds(false);
                printSettings.setColorModel(ColorModel.COLOR);
                printSettings.setDuplexMode(DuplexMode.SIMPLEX);
                printSettings.setDisplayHeaderFooter(true);
                printSettings.setCopies(1);
                printSettings.setPaperSize(PaperSize.ISO_A4);

                List<PageRange> ranges = new ArrayList<PageRange>();
                ranges.add(new PageRange(0, 3));
                printSettings.setPageRanges(ranges);

                printJob.addPrintJobListener(new PrintJobListener() {
                    @Override
                    public void onPrintingDone(PrintJobEvent event) {
                        System.out.println("Printing is finished successfully: " +
                                event.isSuccess());
                    }
                });
                return PrintStatus.CONTINUE;
            }
        });

        browser.loadURL("http://www.teamdev.com");
    }
}
