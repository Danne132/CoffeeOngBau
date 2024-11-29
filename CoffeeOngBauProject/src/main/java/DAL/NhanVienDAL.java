package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model_DTO.NhanVien;
import Utils.ComonUtils;
import Utils.DBUtils;

public class NhanVienDAL {
	public boolean AddNew(NhanVien nv) {
	
		return true;
	}
	public boolean Delete(NhanVien nv) {
		return true;
	}
	public boolean Login(String tk, String mk) throws SQLException {
		Connection conn = DBUtils.openConnection();
		
		String sqlSelect = "SELECT * FROM account";
		Statement lenh = conn.createStatement();
		ResultSet ketQua = lenh.executeQuery(sqlSelect);
		// hiện kết quả
		while(ketQua.next()) {
			
			if(tk.equals(ketQua.getString("taikhoan"))&& 
					ComonUtils.hashPassword(mk).equals(ketQua.getString("matkhau")) ) {
				DBUtils.closeConnection(conn);
				return true;
			}
		}
		
		DBUtils.closeConnection(conn);
		return false;
	}
	public boolean createAccount(String tk, String mk) throws SQLException {
		 Connection conn = DBUtils.openConnection();
		
		 String sql = "INSERT INTO account (taikhoan,matkhau) VALUES (?, ?)";
		 PreparedStatement statement = conn.prepareStatement(sql);

         statement.setString(1, tk);    
         statement.setString(2, ComonUtils.hashPassword(mk)); 
         int rowsInserted = statement.executeUpdate();
         if (rowsInserted > 0) {
             System.out.println("Thêm bản ghi thành công!");
             DBUtils.closeConnection(conn);
             statement.close();
             return true;
         }
		DBUtils.closeConnection(conn);
		return false;
	}

}