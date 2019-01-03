package com.hust.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class CoordinatePicker extends JFrame {
	private PWindow app;
	private Container container = getContentPane();

	private JTextField jTextField0, jTextField1, jTextField2;
	private JButton jButton;

	public final String title = "Choose Coordinate";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ActionListener buttonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("setTarget")) {
				app.updateTarget(parseInput());
				dispose();
			}
		}
	};

	private KeyListener keyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				jButton.doClick();
			}
		}
	};

	@SuppressWarnings("unused")
	public CoordinatePicker(PWindow app) {
		this.app = app;
		jTextField0 = new JTextField("x-coordinate");
		jTextField1 = new JTextField("y-coordinate");
		jTextField2 = new JTextField("z-coordinate");
		jTextField0.addKeyListener(keyListener);
		jTextField1.addKeyListener(keyListener);
		jTextField2.addKeyListener(keyListener);
		TextFieldFocusListener textFieldFocusListener0 = new TextFieldFocusListener(jTextField0, "x-coordinate");
		TextFieldFocusListener textFieldFocusListener1 = new TextFieldFocusListener(jTextField1, "y-coordinate");
		TextFieldFocusListener textFieldFocusListener2 = new TextFieldFocusListener(jTextField2, "z-coordinate");
		jButton = new JButton("OK");
		jButton.setActionCommand("setTarget");
		jButton.addActionListener(buttonActionListener);
		jButton.addKeyListener(keyListener);
		container.setLayout(new FlowLayout());
		container.add(jTextField0);
		container.add(jTextField1);
		container.add(jTextField2);
		container.add(jButton);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setVisible(true);
		toFront();
		requestFocus();
		//addFocusListener(new DialogFocusListener());
	}

	private float[] parseInput() {
		float tx, ty, tz;
		try {
			tx = Float.parseFloat(jTextField0.getText());
		} catch (NumberFormatException e) {
			dispose();
			tx = Float.NaN;
		}
		try {
			ty = Float.parseFloat(jTextField1.getText());
		} catch (NumberFormatException e) {
			dispose();
			ty = Float.NaN;
		}
		try {
			tz = Float.parseFloat(jTextField2.getText());
		} catch (NumberFormatException e) {
			dispose();
			tz = Float.NaN;
		}
		return new float[] { tx, ty, tz };
	}

	private class TextFieldFocusListener implements FocusListener {

		private JTextField jtextfield;
		private String promptText = "";

		public TextFieldFocusListener(JTextField jTextField, String promptText) {
			this.jtextfield = jTextField;
			this.jtextfield.addFocusListener(this);
			this.promptText = promptText;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (jtextfield.getText().equals(promptText)) {
				jtextfield.setText("");
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (jtextfield.getText().length() < 1) {
				jtextfield.setText(promptText);
			}
		}

	}
}
