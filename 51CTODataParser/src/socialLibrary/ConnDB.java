package socialLibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnDB {
	private static final String getDriver="com.mysql.jdbc.Driver";
	private static final String getUrl="jdbc:mysql://localhost:3306/51cto";
	private static final String getUser="root";
	private static final String getPwd="1264895271";
	Connection conn=null;
	Statement stat=null;
	ResultSet rs=null;
	//定义一个方法返回Connection对象
	public Connection getConn(){
		try{
			Class.forName(getDriver);
			try{
				conn=DriverManager.getConnection(getUrl, getUser, getPwd);
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		return conn;
	}
	//定义一个方法用于完成增，删，改
	public int getModify(String sql){
		int i=0;
		try{
			stat = getConn().createStatement();
			i=stat.executeUpdate(sql);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try {
				closeStatement(stat);
				closeConnection(getConn());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return i;
	}
	//定义一个方法用于完成查询
	public ResultSet getSelect(String sql){
		try{
			stat=getConn().createStatement();
			rs=stat.executeQuery(sql);
		}
		catch(SQLException e){
			e.printStackTrace();
			}
		return rs;
	}
	//定义一个方法用于关闭Connection
	public void closeConnection(Connection con) throws SQLException{
		if(con!=null&&!con.isClosed()){
			con.close();
		}
	}
	//定义一个方法用于关闭Statement
	public void closeStatement(Statement st) throws SQLException{
		if(st!=null && !st.isClosed()){
			st.close();
		}
	}
	//定义一个方法用于关闭ResuletSet
	public void closeResultSet(ResultSet rss) throws SQLException{
		if(rss!=null)
		{
			rss.close();
			closeStatement(stat);
			closeConnection(conn);
		}
	}
}
