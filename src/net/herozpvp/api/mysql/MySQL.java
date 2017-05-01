package net.herozpvp.api.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;




public class MySQL
{
  private String HOST = "";
  private String DATABASE = "";
  private String USER = "";
  private String PASSWORD = "";
  private Integer Port = 3306;
  private String tablename = "";
  private String MainParm = "";
private HashMap<String, String> MySQLTableRows =  new HashMap<>();
 

  public Connection con;
  
  public MySQL(String host, Integer port, String database, String user, String password)
  {
    this.HOST = host;
    this.DATABASE = database;
    this.USER = user;
    this.PASSWORD = password;
    this.Port = port;
    connect();
  }
  
  public void connect()
  {
    try
    {
      this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":"+ this.Port+ "/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
      
      System.out.println("[MySQL] Connect to Driver Successfully");
    }
    catch (SQLException e)
    {
      System.out.println("[MySQL] Disconnected from Deiver reason :" + e.getMessage());
    }
  }
  
  public void close()
  {
    try
    {
      if (this.con != null)
      {
        this.con.close();
        System.out.println("[MySQL] Disconnected form the driver.");
      }
    }
    catch (SQLException e)
    {
      System.out.println("[MySQL] Erorr while we disconect to MySQL " + e.getMessage());
    }
  }
  
  public void update(String qry)
  {
    try
    {
     
      con.createStatement().executeUpdate(qry);
      con.createStatement().close();
    }
    catch (SQLException e)
    {
      connect();
      System.err.println(e);
    }
  }
  
  public ResultSet query(String qry)
  {
    ResultSet rs = null;
    try
    {
      Statement st = this.con.createStatement();
      rs = st.executeQuery(qry);
    }
    catch (SQLException e)
    {
      connect();
      System.err.println(e);
    }
    return rs;
  }
  
  public String getTablename(){
	  return tablename;
  }
  
  public void setTablename(String i){
	  tablename = i;
  }
  public String getMainParm(){
	  return MainParm;
  }
  
  public void setMainParm(String i){
	  MainParm = i;
  }

 public void addRow(String name, String value){
	 if (!MySQLTableRows.containsKey(name)){
	 MySQLTableRows.put(name, value);
	 }
  }

 private String getRows(){
	 int x = 1;
	 String rows = "";
	 for (String str : MySQLTableRows.keySet()){
		 if (x == MySQLTableRows.size()){
			  rows += str +"";

		 }else{
			  rows += str +", ";

		 }
		 x++;
	 }
	return rows;
 }
 
 private String getRowsVlaues(){
	 int x = 1;
	 String values = "";
	 for (String str : MySQLTableRows.keySet()){
		 if (x == MySQLTableRows.size()){
			  values += "'" +MySQLTableRows.get(str) + "'";

		 }else{
			  values += "'" +MySQLTableRows.get(str) + "', ";

		 }
		 x++;

	 }
	return values;
 }

  public void createTable(String rowsstring){
		update("CREATE TABLE IF NOT EXISTS "+ getTablename() +"("+rowsstring +");");
  }
  
  public boolean checkRowExists(String value)
  {
    try
    {
      ResultSet rs = query("SELECT * FROM "+getTablename()+" WHERE "+ getMainParm() +"= '" + value + "'");
      if (rs.next())
      {
          return rs.getString(getMainParm()) != null;
        }
    return false;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public void createRow(String value)
  {
    if (!checkRowExists(value)) {
      update("INSERT INTO "+ getTablename()+" ("+ getRows() +") VALUES ("+ getRowsVlaues()+");");
    }
  }
  
  public String getString(String checkedvalue, String value)
  {
    String i = "";
    if (checkRowExists(checkedvalue))
    {
      try
      {
        ResultSet rs = query("SELECT * FROM "+ getTablename() + " WHERE "+getMainParm()+"= '" + checkedvalue + "'");
        if ((!rs.next())  ||  (rs.getString(value) == null));
        i = rs.getString(value);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createRow(value);
      getString(checkedvalue, value);
    }
    return i;
  }
  
  public int getInt(String checkedvalue, String value)
  {
    int i = 0;
    if (checkRowExists(checkedvalue))
    {
      try
      {
        ResultSet rs = query("SELECT * FROM "+ getTablename() + " WHERE "+getMainParm()+"= '" + checkedvalue + "'");
        if ((!rs.next())  ||  (rs.getString(value) == null));
        i = rs.getInt(value);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      createRow(value);
      getInt(checkedvalue, value);
    }
    return i;
  }
  
  public void setValue(String checkedvalue, String Cloumnname, Object value)
  {
	    if (checkRowExists(checkedvalue)){

     update("UPDATE "+getTablename()+" SET " + Cloumnname+"= '" + value + "' WHERE "+ getMainParm()+"= '" + checkedvalue + "';");
    }
    else 
    {
        createRow(checkedvalue);
        setValue(checkedvalue, Cloumnname, value);
        }
  }
  
  
  

}