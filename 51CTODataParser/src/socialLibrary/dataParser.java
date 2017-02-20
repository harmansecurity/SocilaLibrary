package socialLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class dataParser {

/**
 * 以行为单位读取文件，常用于读面向行的格式化文件
 * @param fileName
 */
	public static void readFileByLines(String fileName){
		File file = new File(fileName);
		String userName=null;
		String  userPwd=null;
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
	
	public static void main(String[] args) {
		//数据插入到数据库中
		//readFileByLines("G:/信息安全/dataParser/51cto.txt");
		//查询数据
		String data;
		System.out.println("请输入你要查询的用户名或者邮箱：");
		Scanner input = new Scanner(System.in);
		data=input.next();
		operateData.queryData(data);
	}
}
