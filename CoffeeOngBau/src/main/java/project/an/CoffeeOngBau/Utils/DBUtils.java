package project.an.CoffeeOngBau.Utils;

import java.sql.*;
import java.util.HashMap;

public class DBUtils {
	public static Connection openConnection(String dbName, String user, String pass) {
	    Connection con = null;
	    try {
	      // Đăng ký driver
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      // Chuẩn bị chuỗi kết nối
	      String connectionURL = "jdbc:mysql://localhost:3306/"+ dbName;
	      // Mở kết nối
	      con = DriverManager.getConnection(connectionURL, user, pass);
	      System.out.println("Connection successful!");
	    } catch (ClassNotFoundException e) {
	      System.out.println("MySQL Driver not found!");
	      e.printStackTrace();
	    } catch (SQLException e) {
	      System.out.println("Connection failed!");
	      e.printStackTrace();
	    }
	    return con;
	  }
	public static void closeConnection(Connection con) {
	    if (con != null) {
	      try {
	        con.close();
	        System.out.println("Connection closed!");
	      } catch (SQLException e) {
	        System.out.println("Failed to close connection!");
	        e.printStackTrace();
	      }
	    }
	}
	public static HashMap<String, String> getCategoryFromDB(String sql, String maLoaiOb, String tenLoaiOb)  {
		HashMap<String, String> loai = new HashMap<>();
		Connection conn = DBUtils.openConnection("banhang", "root", "");
		String sqlSelect = sql;
		Statement lenh = null;
		try {
			lenh = conn.createStatement();
			ResultSet ketQua = lenh.executeQuery(sqlSelect);
			while(ketQua.next()){
				String maLoai = ketQua.getString(maLoaiOb);
				String tenLoai = ketQua.getString(tenLoaiOb);
				loai.put(maLoai, tenLoai);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DBUtils.closeConnection(conn);
		return loai;
	}
}
