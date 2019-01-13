package com.hust.view.javafx;

import java.net.URL;
import java.util.ResourceBundle;

import com.hust.core.Configurations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class PreloaderController implements Initializable {

	@FXML
	private Label supervisorLabel, author1Label, author2Label, author3Label;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		author1Label.setText("Trần Hoàng Nhật");
		if (Boolean.parseBoolean(Configurations.PROPERTIES.getProperty("monthaytuanden"))) {
			supervisorLabel.setText("Dr. Trần Văn Tuấn");
			author2Label.setText("Nguyễn Tiến Nam");
			author3Label.setText("Tô Tiến Thành");
		} else {
			supervisorLabel.setText("Dr. Nguyễn Cảnh Quang");
			author2Label.setVisible(false);
			author3Label.setVisible(false);
		}
	}

}
