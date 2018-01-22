package com.biliyor.gui;

import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {

	public static ImageIcon createIcon(String path) {

		URL url = System.class.getResource(path);

		if (url == null) {
			System.out.println("Unable to open icon");
		}

		ImageIcon icon = new ImageIcon(url);
		return icon;
	}

}
