package com.biliyor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static javax.swing.GroupLayout.Alignment.*;

public class SearchPanel extends JPanel {

	private TablePanel dbTablePanel;
	private static SearchPanel instance;

	public SearchPanel() {

		JLabel label = new JLabel("Filter record:");
		;
		JTextField textSearch = new JTextField();
		JCheckBox caseCheckBox = new JCheckBox("From Name");
		JCheckBox wrapCheckBox = new JCheckBox("From Date");
		JCheckBox wholeCheckBox = new JCheckBox("From Action");

		JButton findButton = new JButton("Find");

		dbTablePanel = TablePanel.getInstance();

		// button search row
		findButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				dbTablePanel.searchDbByName(textSearch.getText());
			}
		});

		// remove redundant default border of check boxes - they would hinder
		// correct spacing and aligning (maybe not needed on some look and feels)
		caseCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		wrapCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		wholeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(label)
				.addGroup(layout.createParallelGroup(LEADING).addComponent(textSearch)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(LEADING).addComponent(caseCheckBox)
										.addComponent(wholeCheckBox))
								.addGroup(layout.createParallelGroup(LEADING).addComponent(wrapCheckBox))))
				.addGroup(layout.createParallelGroup(LEADING).addComponent(findButton)));

		layout.linkSize(SwingConstants.HORIZONTAL, findButton);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(BASELINE).addComponent(label).addComponent(textSearch)
						.addComponent(findButton))
				.addGroup(layout.createParallelGroup(LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(BASELINE).addComponent(caseCheckBox)
										.addComponent(wrapCheckBox))
								.addGroup(layout.createParallelGroup(BASELINE).addComponent(wholeCheckBox)))));
	}

	public static SearchPanel getInstance() {

		if (instance == null) {
			instance = new SearchPanel();
		}
		return instance;
	}

}