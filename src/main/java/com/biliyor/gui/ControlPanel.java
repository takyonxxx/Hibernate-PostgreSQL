package com.biliyor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.biliyor.entity.HibernateConnector;

public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ControlPanel instance;

	private JTextField textId;
	private JTextField textName;
	private JTextField textDate;

	private JComboBox<?> comboCity;

	private JLabel lId;
	private JLabel lName;
	private JLabel lCity;
	private JLabel lDate;

	private JButton btnAdd;
	private JButton btnDelete;
	private JButton btnUpdate;

	private TablePanel dBHiberTable;

	public static ControlPanel getInstance() {

		if (instance == null) {
			instance = new ControlPanel();
		}
		return instance;
	}

	public ControlPanel() {

		setLayout(new BorderLayout());

		add(initButtons(), BorderLayout.CENTER);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dBHiberTable = TablePanel.getInstance();

					ListSelectionModel cellSelectionModel = dBHiberTable.getSelectionModel();
					cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent listSelectionEvent) {

							if (listSelectionEvent.getValueIsAdjusting() || isEmpty(dBHiberTable))
								return;
							ListSelectionModel lsm = (ListSelectionModel) listSelectionEvent.getSource();
							if (lsm.isSelectionEmpty()) {
								System.out.println("No rows selected");
							} else {
								try {
									
									int row = lsm.getMinSelectionIndex();
									System.out.println("rows selected: " + row);
									if (row >= 0) {
										
										String id = dBHiberTable.getModel().getValueAt(row, 0).toString();	
										String name = dBHiberTable.getModel().getValueAt(row, 1).toString();	
										String city = dBHiberTable.getModel().getValueAt(row, 2).toString();	
										String date = dBHiberTable.getModel().getValueAt(row, 3).toString();	
										
										settextFields(id, name, city, date);
									}
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
							}
						}
					});

					dBHiberTable.searchDbByName("");

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String sDate = dateFormat.format(date);
		textDate.setText(sDate);

	}
	
	private void settextFields(String id, String name, String city, String date) {
		
		textId.setText(id);
		textName.setText(name);
		comboCity.getModel().setSelectedItem(city);
		textDate.setText(date);
	}

	private static boolean isEmpty(JTable jTable) {
		if (jTable != null && jTable.getModel() != null) {
			return jTable.getModel().getRowCount() <= 0 ? true : false;
		}
		return false;
	}

	private JPanel initButtons() {

		JPanel panel = new JPanel();

		// create JLabels
		lId = new JLabel("Id:");
		lName = new JLabel("Name:");
		lCity = new JLabel("City:");
		lDate = new JLabel("Date:");

		// create JTextFields
		textId = new JTextField(10);
		textName = new JTextField(15);
		textDate = new JTextField(15);

		comboCity = new JComboBox<Object>(CityNames.values());

		textId.setEnabled(false);
		textId.setBackground(Color.GRAY);

		// create JButtons
		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");

		btnAdd.setIcon(Utils.createIcon("/images/Save16.gif"));
		btnDelete.setIcon(Utils.createIcon("/images/Delete16.gif"));
		btnUpdate.setIcon(Utils.createIcon("/images/Refresh16.gif"));

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dBHiberTable.setName(textName.getText());
				dBHiberTable.setCity(comboCity.getSelectedItem().toString());
				dBHiberTable.setDate(textDate.getText());
				dBHiberTable.addDb();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dBHiberTable.deleteDb(Integer.parseInt(textId.getText()));
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dBHiberTable.setName(textName.getText());
				dBHiberTable.setCity(comboCity.getSelectedItem().toString());
				dBHiberTable.setDate(textDate.getText());
				dBHiberTable.updtaeDb(Integer.parseInt(textId.getText()));
			}
		});

		panel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		//// first row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.gridy = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(lId, gc);

		gc.gridy = 0;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(textId, gc);

		/// second row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 1;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(lName, gc);

		gc.gridy = 1;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(textName, gc);

		/// third row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 2;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(lCity, gc);

		gc.gridy = 2;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(comboCity, gc);

		/// forth row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 3;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(lDate, gc);

		gc.gridy = 3;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(textDate, gc);

		/// first button

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 4;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(btnAdd, gc);

		/// second button

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 5;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(btnDelete, gc);

		/// third button

		gc.weightx = 1;
		gc.weighty = 2;

		gc.gridy = 6;
		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(btnUpdate, gc);

		return panel;
	}
}