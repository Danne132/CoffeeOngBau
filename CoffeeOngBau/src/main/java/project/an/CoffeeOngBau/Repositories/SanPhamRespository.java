package project.an.CoffeeOngBau.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;

public class SanPhamRespository {
    private Connection conn;
    public ObservableList<SanPham> getAllSP(HashMap<String, String> loaisps){
        ObservableList<SanPham> spList = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM sanpham";
        try {
            PreparedStatement prepare = conn.prepareStatement(sqlSelect);
            ResultSet result = prepare.executeQuery(sqlSelect);
            SanPham sp;
            while(result.next()){
                sp = new SanPham(
                        result.getString("maSP"),
                        result.getNString("tenSP"),
                        loaisps.get(result.getString("loaiSP")),
                        result.getNString("moTa"),
                        result.getNString("ghiChu"),
                        result.getBoolean("trangThai")?"Đang bán":"Ngừng bán",
                        result.getString("anhSP"),
                        result.getInt("donGia")
                );
                spList.add(sp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return spList;
    }
    public void addSP(SanPham sanPham){
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlInsert = "INSERT INTO `sanpham` (`maSP`, `tenSP`, `loaiSP`, `donGia`, `anhSP`, `moTa`, `ghiChu`, `trangThai`) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement lenh = conn.prepareStatement(sqlInsert);
            lenh.setString(1, sanPham.getMaSP());
            lenh.setString(2, sanPham.getTenSP());
            lenh.setString(3, sanPham.getTenSP());
            lenh.setDouble(4, sanPham.getDonGia());
            lenh.setString(5, sanPham.getAnhSP());
            lenh.setString(6, sanPham.getMoTa());
            lenh.setString(7, sanPham.getGhiChu());
            boolean tt = sanPham.getTrangThai()=="Đang bán";
            lenh.setBoolean(8, tt);
            int rowsInserted = lenh.executeUpdate();
            Optional<ButtonType> confirmAction = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn thêm sản phẩm này?");
            if (rowsInserted > 0) {
                setAlert(Alert.AlertType.INFORMATION, "Thêm sản phẩm", "Thêm sản phẩm thành công");
            } else {
                setAlert(Alert.AlertType.INFORMATION, "Thêm sản phẩm", "Thêm sản phẩm không thành công");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }
}
