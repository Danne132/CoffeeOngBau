package project.an.CoffeeOngBau.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import project.an.CoffeeOngBau.Models.Entities.NhanVien;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.ComonUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;
import static project.an.CoffeeOngBau.Utils.DBUtils.getCategoryFromDB;

public class NhanVienRepository implements INhanVienRepository{
    Connection conn;
    private HashMap<String, String> loainvs;
    public NhanVienRepository() {
        loainvs = getCategoryFromDB("SELECT * FROM chucvunv", "maCV", "tenCV");
    }

    public HashMap<String, String> getLoainvs() {
        return loainvs;
    }

    @Override
    public void addNV(NhanVien nv) {
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlInsert = "INSERT INTO `nhanvien` (`maNV`, `tenNV`, `gioiTinh`, `ngaySinh`, `chucVu`, `SDT`, `email`, `anhNV`, `isWorking`, `username`, `password`) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement lenh = conn.prepareStatement(sqlInsert);
            lenh.setString(1, nv.getId());
            lenh.setString(2, nv.getTenNV());
            lenh.setBoolean(3, nv.isGioiTinh());
            lenh.setDate(4, nv.getNgaySinh());
            lenh.setString(5, nv.getChucVu());
            lenh.setString(6, nv.getSDT());
            lenh.setString(7, nv.getEmail());
            lenh.setString(8, nv.getAnhNV());
            lenh.setBoolean(9, nv.getIsWorking().equals("Đang làm"));
            lenh.setString(10, nv.getEmail());
            lenh.setString(11, ComonUtils.hashPassword(nv.getSDT()));
            int rowsInserted = lenh.executeUpdate();
            if (rowsInserted > 0) {
                setAlert(Alert.AlertType.INFORMATION, "Thêm", "Thêm nhân viên thành công");
            } else {
                setAlert(Alert.AlertType.INFORMATION, "Thêm", "Thêm nhân viên không thành công");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }

    public Connection getConn() {
        return conn;
    }

    public ObservableList<NhanVien> getAllNVList(){
        ObservableList<NhanVien> nvList = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM nhanvien";
        try {
            PreparedStatement prepare = conn.prepareStatement(sqlSelect);
            ResultSet result = prepare.executeQuery(sqlSelect);
            NhanVien nhanVien;
            while(result.next()){
                nhanVien = new NhanVien(
                        result.getString("maNV"),
                        result.getNString("tenNV"),
                        loainvs.get(result.getString("chucVu")),
                        result.getNString("SDT"),
                        result.getNString("email"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("anhNV"),
                        result.getBoolean("isWorking")?"Đang làm":"Nghỉ làm",
                        result.getTimestamp("createdAt"),
                        result.getTimestamp("updatedAt"),
                        result.getBoolean("gioiTinh"),
                        result.getDate("ngaySinh")
                );
                nvList.add(nhanVien);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return nvList;
    }

    public void deleteNV(){
        Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn xóa thông tin nhân viên này?");
        if(optional.get().equals(ButtonType.OK)){
            try {
                String sqlDelete = "DELETE FROM `nhanvien` WHERE `maNV`='"+ current_data.id+"'";
                conn = DBUtils.openConnection("banhang", "root", "");
                PreparedStatement prepare = conn.prepareStatement(sqlDelete);
                prepare.executeUpdate();
                setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã xóa thông tin nhân viên này!");
                DBUtils.closeConnection(conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xóa");
    }

    public String setAutoMaNV(ComboBox<String> cbb){
        String getChucVu = getMaCV(cbb);
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT maNV FROM nhanvien WHERE chucVu LIKE ? ORDER BY maNV DESC LIMIT 1";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, getChucVu + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return getChucVu + "001";
                }
                String lastMaNV = resultSet.getString("maNV");
                System.out.println("Mã lớn nhất: " + lastMaNV);
                int number = Integer.parseInt(lastMaNV.substring(getChucVu.length()));
                return getChucVu + String.format("%03d", number + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    public String getMaCV(ComboBox<?> cbb) {
        String maLoai = null;
        for(String key : loainvs.keySet()){
            if(loainvs.get(key) == cbb.getSelectionModel().getSelectedItem()){
                maLoai = key;
                return maLoai;
            }
        }
        System.out.println(maLoai);
        return maLoai;
    }

    public boolean Login(String account, String password) throws SQLException {
        if (account.isEmpty() || password.isEmpty()) {
            setAlert(Alert.AlertType.ERROR, "Lỗi đăng nhập", "Tên tài khoản và mật khẩu không để trống!");
            return false;
        }
        Connection conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM nhanvien";
        Statement lenh = conn.createStatement();
        ResultSet ketQua = lenh.executeQuery(sqlSelect);
        while(ketQua.next()) {

            if(account.equals(ketQua.getString("username"))&&
                    ComonUtils.hashPassword(password).equals(ketQua.getString("password")))
            {
                current_data.username = ketQua.getString("tenNV");
                current_data.chucVu = ketQua.getString("chucVu");
                current_data.userid = ketQua.getString("maNV");
                return true;
            }
        }
        DBUtils.closeConnection(conn);
        return false;
    }
}
