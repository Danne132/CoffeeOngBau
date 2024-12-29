package project.an.CoffeeOngBau.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;
import static project.an.CoffeeOngBau.Utils.DBUtils.getCategoryFromDB;

public class SanPhamRespository {
    private Connection conn;
    private HashMap<String, String> loaisps;

    public SanPhamRespository() {
        loaisps = getCategoryFromDB("SELECT * FROM loaisp", "maLoai", "tenLoai");
    }
    public HashMap<String, String> getLoaiSPS(){
        return loaisps;
    }
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
            lenh.setString(3, sanPham.getLoaiSP());
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
        finally {
            DBUtils.closeConnection(conn);
        }

    }
    public void updateSP(SanPham sanPham){
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlUpdate = "UPDATE `sanpham` SET `tenSP`=?,`loaiSP`=?,`donGia`=?,`anhSP`=?,`moTa`=?,`ghiChu`=?,`trangThai`=? WHERE `maSP`=?";
        try {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn có chắc muốn cập nhật thông tin của mặt hàng " + sanPham.getTenSP() + "?");
            if(optional.get().equals(ButtonType.OK)){
                PreparedStatement lenh = conn.prepareStatement(sqlUpdate);
                lenh.setString(1, sanPham.getTenSP());
                lenh.setString(2, sanPham.getLoaiSP());
                lenh.setDouble(3, sanPham.getDonGia());
                lenh.setString(4, sanPham.getAnhSP());
                lenh.setString(5, sanPham.getMoTa());
                lenh.setString(6, sanPham.getGhiChu());
                boolean tt = sanPham.getTrangThai()=="Đang bán";
                lenh.setBoolean(7, tt);
                lenh.setString(8, current_data.id);
                lenh.executeUpdate();
                setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Cập nhật thông tin thành công");
            } else {
                setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã hủy cập nhật!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.closeConnection(conn);
        }
    }
    public void deleteSP(){
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlDelete = "DELETE FROM `sanpham` WHERE `maSP`='"+current_data.id+"'";
        Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn xóa sản phẩm này?");
        if(optional.get().equals(ButtonType.OK)){
            try {
                PreparedStatement prepare = conn.prepareStatement(sqlDelete);
                prepare.executeUpdate();
                setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã xóa sản phẩm này!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                DBUtils.closeConnection(conn);
            }
        } else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xóa sản phẩm");
    }
    public String setAutoMaSP(ComboBox<String> cbb){
        String getMaLoai = getMaLoai(cbb);
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT maSP FROM sanpham WHERE loaiSP LIKE ? ORDER BY maSP DESC LIMIT 1";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, getMaLoai + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return getMaLoai + "001";
                }
                String lastMaNV = resultSet.getString("maSP");
                System.out.println("Mã lớn nhất: " + lastMaNV);
                int number = Integer.parseInt(lastMaNV.substring(getMaLoai.length()));
                return getMaLoai + String.format("%03d", number + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }
    public String getMaLoai(ComboBox<String> cbb){
        String maLoai = null;
        for(String key : loaisps.keySet()){
            if(loaisps.get(key) == cbb.getSelectionModel().getSelectedItem()){
                maLoai = key;
                return maLoai;
            }
        }
        System.out.println(maLoai);
        return maLoai;
    }
}
