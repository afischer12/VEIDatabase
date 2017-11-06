import java.io.FileNotFoundException;
import java.sql.*;
import net.ucanaccess.jdbc.UcanaccessSQLException;

@SuppressWarnings("serial")
public class EditDatabase extends NewEntryForm{

	public static int addLine(String tableName, String[] columns, String[] entries) throws Exception{
		int exitStatus=0;
		String columnNames="";
		String entryNames="";
		for(int counter=0;counter<columns.length;counter++) {
			columnNames+=columns[counter]+",";
			if(counter==0&&SQLQuery("SELECT * FROM "+tableName).getMetaData().getColumnCount()==1) {
				SQLUpdate("ALTER TABLE "+tableName+" DROP PRIMARY KEY");
				SQLUpdate("ALTER TABLE "+tableName+" ADD COLUMN "+columns[counter]+" TEXT(64) NOT NULL ");
			}else {
				SQLUpdate("ALTER TABLE "+tableName+" ADD COLUMN "+columns[counter]+" TEXT(64)");
			}
			entryNames+=entries[counter]+",";
		}
		columnNames=columnNames.substring(0, columnNames.length()-1);
		entryNames=entryNames.substring(0,entryNames.length()-1);
		exitStatus=SQLUpdate("INSERT INTO "+tableName+"("+columnNames+") ENTRIES ("+entryNames+")");
		return exitStatus;
	}
	
	public static int addTable(String tableName, String[][] columns) {
		int exitStatus=0;
		String columnNames="";
		
		return exitStatus;
		
	}
	public static int SQLUpdate(String update) {
		int exitStatus=0;
		try {
		    Connection con = DriverManager.getConnection("jdbc:ucanaccess://Z:/test.accdb");
	 	    Statement st=con.createStatement();
		    con.setAutoCommit(false);
			int i=st.executeUpdate(update);
			con.commit(); 
			con.close(); 
		}catch(Exception e) {
			if(e instanceof SQLException){
				exitStatus=1;
			}else if(e instanceof ClassNotFoundException){
				exitStatus=2;
			}else {
				exitStatus=3;
			}
		}
		return exitStatus;
	}
	public static ResultSet SQLQuery(String query) throws Exception {
		ResultSet result = null;
		try {
		    Connection con = DriverManager.getConnection("jdbc:ucanaccess://Z:/test.accdb");
	 	    Statement st=con.createStatement();
			result=st.executeQuery(query);
		}catch(Exception e) {
			throw e;
		}
		return result;
	}

	public static void createTableIfNotExist(String tableName){
		try {
			ResultSet getExist = SQLQuery("SELECT MSysObjects.*, MSysObjects.Type FROM MSysObjects WHERE (((MSysObjects.Type)=1)) OR (((MSysObjects.Type)=6))");
			ResultSetMetaData rmsd = getExist.getMetaData();
			boolean exist=false;
			int columnsNumber = rmsd.getColumnCount();
			while(getExist.next()) {
				for(int c=1;c<=columnsNumber; c++) {
					if(c>1) System.out.print(", ");
					String columnValue=getExist.getString(c);
					System.out.print(columnValue+" "+rmsd.getColumnName(c));
				}
				System.out.println("");
			}
		}catch(Exception e){
			if(e instanceof UcanaccessSQLException) {
				if(e.getCause() instanceof FileNotFoundException) {
					System.err.println("The Database file cannot be accessed or does not exist.");
				}
					
			}
		}
		
	}
	public static void createColumnsIfNotExist(String tableName,String[] columnNames) {
		for(int c=0;c<columnNames.length;c++) {
			
		}
	}
}
