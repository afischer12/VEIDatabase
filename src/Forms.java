import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.sql.SQLException;
import java.util.regex.*;
@SuppressWarnings("serial")
public class Forms extends JFrame implements ActionListener{
	JLabel test;
	JTextPane dataFields;
	JScrollPane textScroll;
	JLabel dataGuide;
	JLabel pName;
	JTextField pNameField;
	JButton createProduct;
	Forms guide;
	Forms(String formType){
		if(formType.equals("entry")) {
			addNewEntry();
		}else if(formType.equals("product")) {
			addNewProduct();
		}
	}
	
	public void addNewProduct(){
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Database Manager");
		setBounds(0,0,450,450);
		setLayout(null);
		test = new JLabel("<html>Enter specific data about the product in the box below. Use the guide on the side to format the data.</html>");
		test.setBounds(getWidth()/2-150, -25, 300, 100);
		test.repaint();
		dataFields = new JTextPane();
		textScroll = new JScrollPane(dataFields);
		textScroll.setBounds(0,75, getWidth()-10, getHeight()-200);
		textScroll.repaint();
		pName = new JLabel("<html>Enter Product Name:</html>");
		pName.setBounds(5,getHeight()-95,125,15);
		pName.repaint();
		pNameField = new JTextField();
		pNameField.setBounds(125, getHeight()-100, 300, 25);
		pNameField.repaint();
		createProduct = new JButton("Confirm");
		createProduct.setToolTipText("Click this button to confirm adding a new product");
		createProduct.setBounds(getWidth()/2-50,getHeight()-75 , 100, 25);
		createProduct.addActionListener(this);
		createProduct.repaint();
		add(test);
		add(textScroll);
		add(pName);
		add(pNameField);
		add(createProduct);
		setVisible(true);
		Forms guide = new Forms("");
		guide.productGuide();
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
		    	guide.dispose();
		    }
		});
	}
	
	public void addNewEntry() {
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Database Manager");
		setSize(450,450);
		setLayout(null);
		test = new JLabel(">>");
		test.setBounds(getWidth()/2-50, getHeight()/4-50, 100 , 100);
		test.repaint();
		add(test);
		setVisible(true);
	}
	
	public void productGuide() {
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Data Formatting Guide");
		setBounds(450,0,450,350);
		setLayout(null);
		dataGuide = new JLabel();
		dataGuide.setText("<html>Use this format when entering product data: <br><br> data name;data value <br><br> ex. Product Number;01234567 <br><br> ONLY PUT PERMANENT PRODUCT DATA VALUES HERE, LIKE THE PRODUCT NUMBER!!!<br><br>Put each one on a seperate line. The semicolon is important. Also, this field will automatically have the fields you have added previously. To add more, simply add a new field on a new line. You will have to give the data each time.</html>");
		dataGuide.setBounds(0, -45, getWidth(), getHeight());
		dataGuide.setFont(new Font("Sans Serif", Font.BOLD, 15));
		dataGuide.repaint();
		add(dataGuide);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String encoding="UTF-8";
		Pattern reg1 = Pattern.compile(".+;",Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.CANON_EQ);
		Pattern reg2 = Pattern.compile(".+",Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.CANON_EQ);
		if(e.getSource().equals(createProduct)) {
			JFrame frame=new JFrame();
			int n=JOptionPane.showOptionDialog(frame, "Are you sure you want to create the product "+pNameField.getText()+" with the inputted data?", "Confimation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,null,null);
			if(n==0) {
				try(PrintStream out=new PrintStream("Z:\\"+pNameField.getText()+".txt",encoding)){
					Matcher match = reg1.matcher(dataFields.getText());
					int arraySize=0;
					String backup = dataFields.getText();
					match.find();
					do{
						dataFields.setText(dataFields.getText().substring(match.end(),dataFields.getText().length()));
						match.usePattern(reg2);
						match.reset(dataFields.getText());
						match.find();
						if(dataFields.getText().length()>0) {
							dataFields.setText(dataFields.getText().substring(match.end(),dataFields.getText().length()));
						}
						match.usePattern(reg1);
						match.reset(dataFields.getText());
						arraySize++;
					}while(match.find());
					String[][] data = new String[arraySize][2];
					dataFields.setText(backup);
					match.usePattern(reg1);
					match.reset(dataFields.getText());
					match.find();
					int columnNo = 0;
					do{
						data[columnNo][0]=dataFields.getText().substring(match.start(),match.end()-1);
						out.print("{Data Field Name:"+dataFields.getText().substring(match.start(),match.end()-1));
						dataFields.setText(dataFields.getText().substring(match.end(),dataFields.getText().length()));
						match.usePattern(reg2);
						match.reset(dataFields.getText());
						match.find();
						out.println(",Data Value:"+dataFields.getText().substring(match.start(),match.end())+"}");
						data[columnNo][1]=dataFields.getText().substring(match.start(),match.end());
						columnNo++;
						//out.print("Full length:"+dataFields.getText().length()+", End Index:"+match.end());
						if(dataFields.getText().length()>0) {
							dataFields.setText(dataFields.getText().substring(match.end(),dataFields.getText().length()));
						}
						match.usePattern(reg1);
						match.reset(dataFields.getText());
					}while(match.find());
					try {
						EditDatabase.addLine("Test", data[0], data[1]);
					} catch (Exception e1) {
						if(e1 instanceof FileNotFoundException) {
							System.err.println("Database File not found");
						}
					}
					dispose();
				} catch (FileNotFoundException e1) {
					System.out.println("The system cannot find the drive where it stores the product data. Make sure the computer that hosts the drive is booted and make sure the folder where the database is is mapped as a network drive with the drive letter Z:.");
				} catch (UnsupportedEncodingException e1) {
					
					System.out.println("Encoding error. You should not see this error, however if you do, simply hit ok and you should be fine.");
				}
			}
		}
	}

	private void createTableIfNotExist(String string) {
		// TODO Auto-generated method stub
		
	}
}
