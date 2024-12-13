import java.sql.SQLException;
import java.util.Scanner;

import BLL.NhanVienBLL;
import Utils.ComonUtils;

public class Main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				NhanVienBLL nv1 = new NhanVienBLL();
				Scanner scanner = new Scanner(System.in);
				System.out.print("Tài Khoản: ");
				String tk = scanner.nextLine();
				
				System.out.print("Mật khẩu: ");
				String mk = scanner.nextLine();
				
//				nv1.dangKiTaiKhoan(tk, mk);


				System.out.println(ComonUtils.encodePas(mk));
				
				if(nv1.checkLogin(tk, mk) == true) {
					System.out.println("Đăng nhập thành công");
					
				}else {
					System.out.println("Đăng nhập thất bại");
				}
	}

}
