/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
import com.teamdev.jxbrowser.chromium.SpellCheckerService;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * The sample demonstrates how to work with spellchecker API.
 */
public class SpellCheckerSample {
    public static void main(String[] args) throws Exception {
        // Enable heavyweight popup menu for heavyweight (default) BrowserView component.
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        BrowserContext context = browser.getContext();
        SpellCheckerService spellCheckerService = context.getSpellCheckerService();
        // Enable SpellChecker service.
        spellCheckerService.setEnabled(true);
        // Configure SpellChecker's language.
        spellCheckerService.setLanguage("en-US");

        browser.setContextMenuHandler(new MyContextMenuHandler(view, browser));
        browser.loadHTML("<html><body><textarea rows='20' cols='30'>" +
                "Smple text with mitake.</textarea></body></html>");
    }

    private static class MyContextMenuHandler implements ContextMenuHandler {

        private final JComponent component;
        private final Browser browser;

        private MyContextMenuHandler(JComponent parentComponent, Browser browser) {
            this.component = parentComponent;
            this.browser = browser;
        }

        private static JMenuItem createMenuItem(String title, final Runnable action) {
            JMenuItem result = new JMenuItem(title);
            result.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action.run();
                }
            });
            return result;
        }

        public void showContextMenu(final ContextMenuParams params) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JPopupMenu popupMenu = createPopupMenu(params);
                    Point location = params.getLocation();
                    popupMenu.show(component, location.x, location.y);
                }
            });
        }

        private JPopupMenu createPopupMenu(final ContextMenuParams params) {
            final JPopupMenu result = new JPopupMenu();
            // Add suggestions menu items.
            List<String> suggestions = params.getDictionarySuggestions();
            for (final String suggestion : suggestions) {
                result.add(createMenuItem(suggestion, new Runnable() {
                    public void run() {
                        browser.replaceMisspelledWord(suggestion);
                    }
                }));
            }
            if (!suggestions.isEmpty()) {
                // Add the "Add to Dictionary" menu item.
                result.addSeparator();
                result.add(createMenuItem("Add to Dictionary", new Runnable() {
                    public void run() {
                        String misspelledWord = params.getMisspelledWord();
                        browser.addWordToSpellCheckerDictionary(misspelledWord);
                    }
                }));
            }
            return result;
        }
    }
}
