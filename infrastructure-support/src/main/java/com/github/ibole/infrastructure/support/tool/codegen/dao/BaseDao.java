package com.github.ibole.infrastructure.support.tool.codegen.dao;

import com.github.ibole.infrastructure.support.tool.codegen.conf.FreeMarkerEnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;



public class BaseDao  {
	static{
		try {
			Properties	jdbc = FreeMarkerEnvConfig.getInstance().getJdbcpro();
			Class.forName(jdbc.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	public Connection getConnection(){
		Properties	jdbc = FreeMarkerEnvConfig.getInstance().getJdbcpro();
		String url  = jdbc.getProperty("url");
		try {
			Connection conn = DriverManager.getConnection(url,jdbc.getProperty("username"),jdbc.getProperty("password"));
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
		}
	}
	public void close(Connection conn,PreparedStatement ps,ResultSet rs) throws SQLException{
		if(ps != null){
			ps.close();
		}
		if(rs != null){
			rs.close();
		}
		if(conn != null){
			conn.close();
		}
	}
	public static void main(String[] args) throws SQLException {
		BaseDao bd  = new BaseDao();
		Connection conn = bd.getConnection();
		System.out.println(conn);
		System.out.println(conn.isClosed());
	}
}
