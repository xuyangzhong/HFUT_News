package com.zxy.crawl.dao;


import java.sql.*;

import com.zxy.crawl.model.News;

public class Dao {
 	static String driver="com.mysql.jdbc.Driver";
 	static String url="jdbc:mysql://localhost:3306/crawl";
 	static String user="root";
 	static String password="1111";
 	
 	
 	String title;
 	String time;
 	String nurl;
 	String content;
 	
	public boolean addTo(News news){
		boolean result = true;
		title=news.getTitle();
		time=news.getTime();
		nurl=news.getUrl();
		content=news.getContent();
		String sql="insert into hfut_news (title,time,url,content) value(?,?,?,?)";
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, user, password);
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, time);
			ps.setString(3, nurl);
			ps.setString(4, content);
			ps.executeUpdate();
		} catch (Exception e) {
			result=false;
			System.out.println("SQL连接出错");
			e.printStackTrace();
		} finally {
			if(ps!=null){
				try {
					ps.close();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return result;
		
	}
	
	
}






