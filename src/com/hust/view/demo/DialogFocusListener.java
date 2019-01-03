package com.hust.view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;

public class DialogFocusListener implements FocusListener {

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		((JFrame) e.getSource()).dispose();
	}

}
