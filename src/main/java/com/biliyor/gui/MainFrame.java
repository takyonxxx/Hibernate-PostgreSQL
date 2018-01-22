package com.biliyor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

//Mnemonics: hafıza geliştirme
//Accelerator: hızlandırıcı

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ControlPanel controlPanel;
	private TablePanel tablePanel;
	private SearchPanel searchPanel;
	private ToolBar toolBar;
	private JSplitPane splitPane;
	private JTabbedPane tabPane;
	private JFileChooser fileChooser;
	private Dimension sizeOffControlPanel;
	private MessagePanel messagePanel;

	public MainFrame() {

		super("Main Database Window");
		setLayout(new BorderLayout());

		setMinimumSize(new Dimension(800, 480));
		setSize(800, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text Files .txt", "txt");
		fileChooser.setFileFilter(filter);

		setJMenuBar(createMenuBar());

		controlPanel = ControlPanel.getInstance();
		searchPanel = SearchPanel.getInstance();
		toolBar = ToolBar.getInstance();
		tablePanel = TablePanel.getInstance();
		tabPane = new JTabbedPane();
		messagePanel = new MessagePanel();
		JScrollPane sTable = new JScrollPane(tablePanel);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controlPanel, tabPane);
		splitPane.setOneTouchExpandable(true);

		tabPane.addTab("Database Table", sTable);
		tabPane.addTab("Messages", messagePanel);

		Border innerBorder = BorderFactory.createTitledBorder("Manage Postgresql Database");
		Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 0, 10);
		controlPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		controlPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Dimension d = controlPanel.getSize();
				Dimension minD = controlPanel.getMinimumSize();
				if (d.width < minD.width)
					d.width = minD.width;
				if (d.height < minD.height)
					d.height = minD.height;
				sizeOffControlPanel = d;
			}
		});

		sTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		add(toolBar, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);
		add(searchPanel, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				tablePanel.disConnectDb();
				dispose();
				System.gc();
			}
		});

		setVisible(true);
	}

	private JMenuBar createMenuBar() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem exportDataItem = new JMenuItem("Export Data");
		JMenuItem importDataItem = new JMenuItem("Import Data");
		JMenuItem exitItem = new JMenuItem("Exit");

		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to exit?",
						"Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {

					WindowListener[] listeners = getWindowListeners();

					for (WindowListener listener : listeners) {
						listener.windowClosing(new WindowEvent(MainFrame.this, 0));
					}
				}
			}
		});

		exportDataItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						tablePanel.saveToFile(file);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Error Export", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		importDataItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						tablePanel.loadFromFile(file);
					} catch (IOException | ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(MainFrame.this, "Error Import", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		fileMenu.add(exportDataItem);
		fileMenu.add(importDataItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		JMenu widowMenu = new JMenu("Settings");
		JMenu showMenu = new JMenu("Show Form Panel");
		JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Show");
		checkItem.setSelected(true);

		checkItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
				if (menuItem.isSelected()) {
					// splitPane.setDividerLocation(splitPane.getLastDividerLocation());
					splitPane.setDividerLocation((int) sizeOffControlPanel.getWidth());
				}
				controlPanel.setVisible(menuItem.isSelected());
			}
		});

		showMenu.add(checkItem);
		widowMenu.add(showMenu);

		menuBar.add(fileMenu);
		menuBar.add(widowMenu);

		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		importDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		exportDataItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

		return menuBar;
	}
}
