
　 　社工库顾名思义就是社工数据库查询工具。网站自称可帮助您判断您的密码或个人信息是否已经被公开或泄漏，即如果您输入自己密码可以在社工库上查到，就意味着你的账号已被盗（过）。这两天正好得到了一些数据，简单做一个社工库。

**如果觉得不错，请先在这个仓库上点个 star 吧**，我会继续跟大家分享自己的学习过程。

##### 简单介绍下MySql的使用
　 　由于我之前的数据库中已经有数据为了方便演示，我把数据删掉，正好也回顾一下MySql的使用。我使用的是Navicat这个支持MySql的可视化工具，由于数据比较多，我直接从CMD终端进行操作删除。

* 很多时候我们电脑的MySQL服务没有开启，`net start mysql`开启MySQL服务。

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%931.png)

* 进入MySQL数据库,在终端中输入`mysql -u root -p`,根据提示输入密码。

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%932.png)

* 查看所有的数据库，`show databases;`我们会看到之前新建的数据库。

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%933.png)

* 使用之前的数据库`use <库名>;`,然后查看当前数据库下面的表,`show tables;`。

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%934.png)

* 删除表里面的所有数据`delete from <表名>;`。

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%935.png)


##### 处理数据，实现保存和查询的功能
　 　我得到的数据是txt格式，每一行是用户名，密码，邮箱，由于这一行数据可能是以多个空格或者分号或者只有用户名没有密码，所以还需要对每一行的数据进行适当的处理然后保存到数据库中。我读取了一下txt文件，txt中有二百多万条数据，大概花了40秒左右，但是将数据存储到数据库中花的时间太长了，由于时间的原因，只向数据库中插入了十万多条数据。

* 对txt文件数据进行处理
```Java
	public static void readFileByLines(String fileName){
		File file = new File(fileName);
		//用户名
		String  userName=null;
		//用户密码
		String  userPwd=null;
		//用户邮箱
		String  userMail=null;
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader=new BufferedReader(new FileReader(file));
			String tempString=null;
			int line=1;
		    //开始时间
			long startTime=System.currentTimeMillis();
			// 一次读入一行，直到读入null为文件结束
			while((tempString=reader.readLine())!=null){
                // 显示每行的数据
               System.out.println("line " + line + ": " + tempString);
                //因为txt文件里每行数据可能是以多个空格或者;来分隔
                String[]  tempArray = tempString.split("\\s+|;");
                //有的数据里面只有用户名或者密码，缺少邮箱，不这样处理的话溢出
                //如果字符串只有用户名和密码，那么字符数组的长度为2
                //那么插入数据到数据库的时候，如果直接赋值tempArray[2]肯定会报错
                if(tempArray.length==2){
                	tempArray=(String[]) Arrays.copyOf(tempArray, tempArray.length+1);
                	tempArray[tempArray.length-1]=" ";
                }
                //System.out.println(tempArray[0]);
                //System.out.println(tempArray[1]);
               // System.out.println(tempArray[2]);
                //把拆分的数据分别赋值然后保存到数据库中
                userName=tempArray[0];
                userPwd=tempArray[1];
                userMail=tempArray[2];
                //把数据插入到数据库中
                operateData.insertData(userName, userPwd, userMail);
                line++;
			}
			//结束时间
			long endTime=System.currentTimeMillis();
			//程序运行的时间
			double seconds=(endTime-startTime)/1000.0;
			System.out.println("程序运行的时间是："+seconds+"s");
			//总共数据量
			System.out.println("总共数据量是："+(line-1));
			reader.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO: handle exception
				}
			}
		}
	}
```
* 将处理完的数据保存到数据库中(插入操作)

```Java
	/**
	 * 将文本中的数据插入到数据库中
	 * @param userName
	 * @param userPwd
	 * @param userMail
	 */
	static void insertData(String userName,String userPwd,String userMail){
		ConnDB cdb = new ConnDB();
		String sql = "insert into userInfo (userName,userPwd,userMail) values ('"+userName+"','"+userPwd+"','"+userMail+"')";
		//执行增删改
		cdb.getModify(sql);
		//不知道是否插入成功，可通过下面的代码来判断
		/*	int i=cdb.getModify(sql);
		  if(i==1)
			System.out.println("添加成功");
		else
			System.out.println("添加失败");*/
	}
```
* 对数据库中的数据进行查询(查询操作)

```Java
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
```

* Eclipse控制台运行效果

![](http://ojto7c1rw.bkt.clouddn.com/%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%936.png)
  
　 　时间的原因，我先暂时不做一个网页了，做一个搜索的网页无非就是一个搜索栏然后连接数据库查询数据，实现起来不是很难。**如果对数据感兴趣的朋友可以私聊我或者留言，到时候转发给你，如果做违法犯罪行为，本人概不负责任。**

----
　 　如果想有更多的了解可以参考我的另一篇文章：**[ELK搭建社工库](http://harmansecurity.cn/2017/02/25/ELK%E6%90%AD%E5%BB%BA%E7%A4%BE%E5%B7%A5%E5%BA%93/)**

-----

# 联系作者

- [Harman's Personal Website](http://harmansecurity.cn/)
- 邮箱：`lianghui_1994@163.com`


-----

# Lisence

Lisenced under [Apache 2.0 lisence](http://opensource.org/licenses/Apache-2.0)

