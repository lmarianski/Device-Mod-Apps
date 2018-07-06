/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * Represents FXML control with address bar and view area that
 * displays URL entered in the address bar text field.
 */
public class BrowserViewControl implements Initializable {

    @FXML
    private TextField textField;

    @FXML
    private BrowserView browserView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        browserView.getBrowser().loadURL(textField.getText());
    }

    public void loadURL(ActionEvent actionEvent) {
        browserView.getBrowser().loadURL(textField.getText());
    }
}
