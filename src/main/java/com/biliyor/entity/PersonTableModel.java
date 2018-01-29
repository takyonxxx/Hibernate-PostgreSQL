package com.biliyor.entity;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.hibernate.sql.SelectValues;

public class PersonTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] colNames = {"Id", "Name", "City", "Date"};
	private Vector<Vector<Object>> tableData;
	Vector<String> tableHeaders;
	
	public PersonTableModel() {			
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colNames[column];
	}	

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public DefaultTableModel getModel() {		
		
		return new DefaultTableModel(tableData, tableHeaders);
	}	

	public void setModel(List<?> resultList) {
		
		tableData = null;
		tableHeaders = null;
		tableData = new Vector<Vector<Object>>();
		tableHeaders = new Vector<String>();
		tableHeaders.add("Id");
		tableHeaders.add("Name");
		tableHeaders.add("City");
		tableHeaders.add("Date");		

		for (Object o : resultList) {
			Person person = (Person) o;
			Vector<Object> oneRow = new Vector<Object>();
			oneRow.add(person.getId());
			oneRow.add(person.getName());
			oneRow.add(person.getAddress().getCity());
			oneRow.add(person.getAddress().getDate());
			tableData.add(oneRow);
		}
	}	
}
