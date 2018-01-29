package com.biliyor.gui;

public interface DbListener {
	public void setConnectionEnable(Boolean enable);

	public void setValues(int id, String name, String city, String date);
}
