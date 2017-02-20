package socialLibrary;

import java.sql.ResultSet;
import java.sql.SQLException;

import socialLibrary.ConnDB;

public class operateData {
	/**
	 * 将文本中的数据插入到数据库中
	 * @param userName
	 * @param userPwd
	 * @param userMail
	 */
	static void insertData(String userName,String userPwd,String userMail){
		ConnDB cdb = new ConnDB();
		String sql = "insert into userInfo (userName,userPwd,userMail) values ('"+userName+"','"+userPwd+"','"+userMail+"')";
		cdb.getModify(sql);
		//不知道是否插入成功，可通过下面的代码来判断
		/*	int i=cdb.getModify(sql);
		  if(i==1)
			System.out.println("添加成功");
		else
			System.out.println("添加失败");*/
	}
	
	/**
	 * 根据用户名或者邮箱查询数据
	 * @param data
	 */
	static void queryData(String data){
		    String userName=null;
	        ConnDB cdb = new ConnDB();
			ResultSet rs = cdb.getSelect("select * from userinfo where userName='"+data+"' or userMail='"+data+"';");
			try {
				while(rs.next()){
					userName=rs.getString("userName");
					System.out.print(rs.getString("userName")+"\t");
					System.out.print(rs.getString("userPwd")+"\t");
					System.out.println(rs.getString("userMail"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(userName==null){
						System.out.println("对不起，您查找的用户不存在！");
					}
					cdb.closeResultSet(rs);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
