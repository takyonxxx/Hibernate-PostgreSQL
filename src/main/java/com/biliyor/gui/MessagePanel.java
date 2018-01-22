package com.biliyor.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

class MessageInfo {

	private int id;
	private String info;

	public MessageInfo(int id, String info) {

		this.id = id;
		this.info = info;
	}

	public int getId() {
		return id;
	}

	public String toString() {
		return info;
	}
}

public class MessagePanel extends JPanel {

	private JTree messageTree;
	private DefaultTreeCellRenderer treeCellRenderer;

	public MessagePanel() {
		
		treeCellRenderer = new DefaultTreeCellRenderer();
		
		treeCellRenderer.setLeafIcon(Utils.createIcon("/images/Forward16.gif"));
		treeCellRenderer.setOpenIcon(Utils.createIcon("/images/Open16.gif"));
		treeCellRenderer.setClosedIcon(Utils.createIcon("/images/Stop16.gif"));

		messageTree = new JTree(createTree());
		messageTree.setCellRenderer(treeCellRenderer);
		messageTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		messageTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) messageTree.getLastSelectedPathComponent();
				
				Object errorObject = node.getUserObject();
			
				if (errorObject instanceof MessageInfo) {
					int id = ((MessageInfo) errorObject).getId();
					String info = errorObject.toString();
				}
			}
		});

		setLayout(new BorderLayout());
		add(new JScrollPane(messageTree), BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createTree() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Messages");
		DefaultMutableTreeNode warnings = new DefaultMutableTreeNode("Warnings");
		DefaultMutableTreeNode errors = new DefaultMutableTreeNode("Errors");

		DefaultMutableTreeNode wmessage = new DefaultMutableTreeNode(new MessageInfo(0, "Warning message 1"));
		DefaultMutableTreeNode emessage = new DefaultMutableTreeNode(new MessageInfo(0, "Error message 1"));

		warnings.add(wmessage);
		errors.add(emessage);

		top.add(warnings);
		top.add(errors);
		return top;
	}
}
