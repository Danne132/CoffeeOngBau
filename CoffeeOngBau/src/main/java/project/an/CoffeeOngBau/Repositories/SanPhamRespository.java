package project.an.CoffeeOngBau.Repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
}
