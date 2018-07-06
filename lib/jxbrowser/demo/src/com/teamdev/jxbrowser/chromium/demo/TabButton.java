/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

package com.teamdev.jxbrowser.chromium.demo;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author TeamDev Ltd.
 */
public class TabButton extends JButton {

    public TabButton(Icon icon, String toolTipText) {
        setIcon(icon);
        setToolTipText(toolTipText);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);
        setFocusable(false);
    }

}
