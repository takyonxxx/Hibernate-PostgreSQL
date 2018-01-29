package com.biliyor.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.biliyor.entity.Address;
import com.biliyor.entity.HibernateConnector;
import com.biliyor.entity.Person;
import com.biliyor.entity.PersonTableModel;

public class TablePanel extends JTable {

	private static TablePanel instance;
	private static Session session = null;
	private String name;
	private String city;
	private String date;
	private PersonTableModel tableModel;
	private ToolBar toolBar;
	public Vector<Vector<Object>> tableData;

	public static TablePanel getInstance() {

		if (instance == null) {
			instance = new TablePanel();
		}
		return instance;
	}

	public TablePanel() {
		// Change A JTable Background Color, Font Size, Font Color, Row Height
		setBackground(Color.LIGHT_GRAY);
		setForeground(Color.black);
		Font font = new Font("", 1, 14);
		setFont(font);
		setRowHeight(25);		
		setRowSelectionAllowed(true);	
		
		tableModel = new PersonTableModel();
		
		toolBar = ToolBar.getInstance();
		toolBar.setDbListener(new DbListener() {

			public void setConnectionEnable(Boolean enable) {
				// TODO Auto-generated method stub
				if (enable)
					connectDb();
				else
					disConnectDb();
			}

			@Override
			public void setValues(int id, String name, String city, String date) {
				setName(name);
				setCity(city);
				setDate(date);				
			}
		});

	}

	public void deleteDb(int id) {

		try {

			Person person = session.get(Person.class, id);

			if (person == null)
				return;

			session.beginTransaction();
			session.delete(person);
			session.getTransaction().commit();

			searchDbByName("");

		} catch (HibernateException he) {
			he.printStackTrace();
		}
	}

	public void addDb() {

		try {

			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate textFieldAsDate = java.time.LocalDate.parse(getDate(), formatter);
			java.sql.Date sqlDate = java.sql.Date.valueOf(textFieldAsDate);

			Address address = new Address(getCity(), sqlDate);
			Person person = new Person(getName(), address);

			session.beginTransaction();
			session.saveOrUpdate(person);
			session.getTransaction().commit();

			searchDbByName("");

		} catch (HibernateException he) {
			he.printStackTrace();
		}

	}

	public void updtaeDb(int id) {

		try {

			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate textFieldAsDate = java.time.LocalDate.parse(getDate(), formatter);
			java.sql.Date sqlDate = java.sql.Date.valueOf(textFieldAsDate);

			Person person = session.get(Person.class, id);

			if (person == null)
				return;

			Address address = person.getAddress();

			address.setCity(getCity());
			address.setDate(sqlDate);
			person.setName(getName());
			person.setAddress(address);

			session.beginTransaction();
			session.saveOrUpdate(person);
			session.getTransaction().commit();

			searchDbByName("");

		} catch (HibernateException he) {
			he.printStackTrace();
		}
	}

	public void searchDbByName(String filter) {

		try {

			session.beginTransaction();
			Query query = session.createQuery("FROM Person where name LIKE name(:name) ");
			query.setParameter("name", "%" + filter + "%");
			List resultList = query.list();
			setTableModel(resultList);
			session.getTransaction().commit();

			if (!isEmpty(this)) {
				// This will set the selection to the last row
				setRowSelectionInterval(getModel().getRowCount() - 1, getModel().getRowCount() - 1);
			}

		} catch (HibernateException he) {
			he.printStackTrace();
		}
	}

	private void setTableModel(List<?> resultList) {

		tableModel.setModel(resultList);
		setModel(tableModel.getModel());
	}

	public void saveToFile(File file) throws IOException {

		TableModel model = getModel();
		FileWriter out = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(out);
		/*
		 * for (int i = 0; i < model.getColumnCount(); i++) {
		 * bw.write(model.getColumnName(i) + ","); } bw.write("\n");
		 */
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				bw.write(model.getValueAt(i, j).toString() + ",");
			}
			bw.write("\n");
		}
		bw.close();
		System.out.print("Write out to" + file);

	}

	public void loadFromFile(File file) throws IOException, ClassNotFoundException {

		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {

				// ((DefaultTableModel) getModel()).addRow(line.split(","));
				String[] parts = line.split(",");

				String name = parts[1];
				String city = parts[2];
				String date = parts[3];

				java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
						.ofPattern("yyyy-MM-dd");
				java.time.LocalDate textFieldAsDate = java.time.LocalDate.parse(date, formatter);
				java.sql.Date sqlDate = java.sql.Date.valueOf(textFieldAsDate);

				// System.out.println(name + " " + city + " " + date);

				Address address = new Address(city, sqlDate);
				Person person = new Person(name, address);

				session.beginTransaction();
				session.saveOrUpdate(person);
				session.getTransaction().commit();
			}
			reader.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error");
			e.printStackTrace();
		}

		searchDbByName("");
	}

	public void connectDb() {

		try {

			session = HibernateConnector.getInstance().getSession();
			toolBar.setlStatus("Db Session Connected...");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void disConnectDb() {
		try {

			session.close();
			// HibernateConnector.getInstance().shutdown();
			toolBar.setlStatus("Db Session Disconnected...");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void shutdownDb() {
		try {

			session.close();
			HibernateConnector.getInstance().shutdown();
			toolBar.setlStatus("Db HibernateConnector has shutdown...");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static boolean isEmpty(JTable jTable) {
		if (jTable != null && jTable.getModel() != null) {
			return jTable.getModel().getRowCount() <= 0 ? true : false;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}