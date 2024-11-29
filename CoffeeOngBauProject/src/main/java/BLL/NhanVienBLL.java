package BLL;

import java.sql.Connection;
import java.sql.SQLException;

import DAL.NhanVienDAL;
import Model_DTO.NhanVien;
import Utils.DBUtils;

public class NhanVienBLL {
	NhanVienDAL nhanVienDAL = new NhanVienDAL();
	boolean ThemMoi(NhanVien nv) {
		boolean kq = nhanVienDAL.AddNew(nv);
		return kq;
	}
	
	public boolean dangKiTaiKhoan(String tenDN, String matkhau) throws SQLException{
		// kiem tra tinh dung dang
		return nhanVienDAL.createAccount(tenDN, matkhau);
	}
	
	public boolean checkLogin(String tenDN, String matKhau) throws SQLException {
		//Kiểm tra tính đúng đắn
		Connection connection = DBUtils.openConnection();
		
		//Vd: tên có rỗng không, có đúng format không
		//Nếu thỏa mãn thì gọi hàm dịch vụ ở tầng DAL
		return nhanVienDAL.Login(tenDN, matKhau);
	}
}

