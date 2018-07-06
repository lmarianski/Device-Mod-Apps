/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType.PRIMARY;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.MOUSE_PRESSED;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.MOUSE_RELEASED;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseEventType.MOUSE_WHEEL;
import static com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseScrollType.WHEEL_BLOCK_SCROLL;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiers;
import com.teamdev.jxbrowser.chromium.BrowserKeyEvent.KeyModifiersBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.BrowserMouseEventBuilder;
import com.teamdev.jxbrowser.chromium.BrowserMouseEvent.MouseButtonType;
import com.teamdev.jxbrowser.chromium.Callback;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * This sample demonstrates how to create and forward mouse events
 * containing scroll parameters, modifiers, and button options to Chromium engine.
 */
public class ForwardMouseEventsSample {
    public static void main(String[] args) {
        final Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JButton scrollDown = new JButton("Scroll Down");
        scrollDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forwardMouseScrollEvent(browser, -1, 11, 62);
            }
        });
        JButton scrollUp = new JButton("Scroll Up");
        scrollUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forwardMouseScrollEvent(browser, 1, 11, 62);
            }
        });
        JButton clickMe = new JButton("Click");
        clickMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forwardMouseClickEvent(browser, PRIMARY, 11, 12, 629, 373);
            }
        });

        JPanel actionPane = new JPanel();
        actionPane.add(scrollDown);
        actionPane.add(scrollUp);
        actionPane.add(clickMe);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(actionPane, BorderLayout.SOUTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Browser.invokeAndWaitFinishLoadingMainFrame(browser, new Callback<Browser>() {
            @Override
            public void invoke(Browser value) {
                browser.loadHTML("<div>" +
                        "<button onclick=\"alert('Mouse has been clicked.');\">Click Me</button></div>"
                        +
                        "<textarea autofocus rows='10' cols='30'>" +
                        "Line 1 \n Line 2 \n Line 3 \n Line 4 \n Line 5 \n " +
                        "Line 6 \n Line 7 \n Line 8 \n Line 9 \n Line 10 \n " +
                        "Line 11 \n Line 11 \n Line 12 \n Line 13 \n Line 14 \n " +
                        "Line 15 \n Line 16 \n Line 17 \n Line 18 \n </textarea>");
            }
        });

    }

    private static void forwardMousePressEvent(Browser browser,
            MouseButtonType buttonType,
            int x,
            int y,
            int globalX,
            int globalY) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_PRESSED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(new KeyModifiersBuilder().mouseButton().build());
        browser.forwardMouseEvent(builder.build());
    }

    private static void forwardMouseReleaseEvent(Browser browser,
            MouseButtonType buttonType,
            int x,
            int y,
            int globalX,
            int globalY) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_RELEASED)
                .setButtonType(buttonType)
                .setX(x)
                .setY(y)
                .setGlobalX(globalX)
                .setGlobalY(globalY)
                .setClickCount(1)
                .setModifiers(KeyModifiers.NO_MODIFIERS);
        browser.forwardMouseEvent(builder.build());
    }

    private static void forwardMouseClickEvent(Browser browser,
            MouseButtonType buttonType,
            int x,
            int y,
            int globalX,
            int globalY) {
        forwardMousePressEvent(browser, buttonType, x, y, globalX, globalY);
        forwardMouseReleaseEvent(browser, buttonType, x, y, globalX, globalY);
    }

    private static void forwardMouseScrollEvent(Browser browser,
            int unitsToScroll,
            int x,
            int y) {
        BrowserMouseEventBuilder builder = new BrowserMouseEventBuilder();
        builder.setEventType(MOUSE_WHEEL)
                .setX(x)
                .setY(y)
                .setGlobalX(0)
                .setGlobalY(0)
                .setScrollBarPixelsPerLine(25)
                .setScrollType(WHEEL_BLOCK_SCROLL)
                .setUnitsToScroll(unitsToScroll);
        browser.forwardMouseEvent(builder.build());
    }
}
