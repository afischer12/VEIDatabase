import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.swing.*;

@SuppressWarnings({ "unused", "serial" })
public class NewEntryForm extends JFrame implements ActionListener{
	JPanel pane = new JPanel();
	JLabel title;
	JLabel select;
	JButton addNewProduct;
	JButton addNewEntry;
	public NewEntryForm(){
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Database Manager");
		setSize(450,450);
		setLayout(null);
		title=new JLabel("<html>Welcome to the <br>Database Manager</htmt>");
		title.setFont(new Font("Papyrus",Font.BOLD ,20));
		title.setBounds(getWidth()/2-100, getHeight()/4-200, 200, 400);
		title.repaint();
		select=new JLabel("Please select an option:");
		select.setFont(new Font("Arial",Font.PLAIN,15));
		select.setBounds(getWidth()/2-80, getHeight()/3, 160, 100);
		select.repaint();
		addNewProduct = new JButton("Add New Product");
		addNewProduct.setToolTipText("Add a new product/bundle to the database");
		addNewProduct.addActionListener(this);
		addNewProduct.setBounds(getWidth()/3-75, getHeight()/2-10, 150, 20);
		addNewProduct.repaint();
		addNewEntry = new JButton("Add New Entry");
		addNewEntry.setToolTipText("Add a new database entry to an existing product table");
		addNewEntry.addActionListener(this);
		addNewEntry.setBounds(getWidth()/3*2-75, getHeight()/2-10, 150, 20);
		addNewEntry.repaint();
		add(title);
		add(select);
		add(addNewProduct);
		add(addNewEntry);
		setVisible(true);
	}
	public static void main(String[] args) {
		new NewEntryForm();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addNewEntry)) {
			Forms newEntry = new Forms("entry");
		}else if(e.getSource().equals(addNewProduct)) {
			Forms newProduct = new Forms("product");
		}
	}
}