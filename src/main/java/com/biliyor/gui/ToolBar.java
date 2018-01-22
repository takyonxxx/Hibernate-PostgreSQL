package com.biliyor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar implements ActionListener {

	private JButton connectButton;
	private JButton disconnectButton;
	private JLabel lStatus;
	private static ToolBar instance;
	private DbListener dbListener;

	public ToolBar() {

		setBorder(BorderFactory.createEtchedBorder());

		connectButton = new JButton();
		disconnectButton = new JButton();

		// setFloatable(false);
		lStatus = new JLabel("");

		add(connectButton);
		// addSeparator();
		add(disconnectButton);
		addSeparator();
		add(lStatus);

		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);

		disconnectButton.setEnabled(true);
		connectButton.setEnabled(false);

		connectButton.setToolTipText("Connect");
		connectButton.setIcon(Utils.createIcon("/images/Forward16.gif"));
		disconnectButton.setToolTipText("Disconnect");
		disconnectButton.setIcon(Utils.createIcon("/images/Stop16.gif"));
	}

	public void setDbListener(DbListener listener) {
		this.dbListener = listener;
	}

	public static ToolBar getInstance() {

		if (instance == null) {
			instance = new ToolBar();
		}
		return instance;
	}

	public void actionPerformed(ActionEvent e) {

		JButton clicked = (JButton) e.getSource();

		if (clicked == connectButton) {
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			dbListener.setConnectionEnable(true);
		} else if (clicked == disconnectButton) {
			disconnectButton.setEnabled(false);
			connectButton.setEnabled(true);
			dbListener.setConnectionEnable(false);
		}
	}

	public String getlStatus() {
		return lStatus.getText();
	}

	public void setlStatus(String lStatus) {
		this.lStatus.setText(lStatus);
	}

}