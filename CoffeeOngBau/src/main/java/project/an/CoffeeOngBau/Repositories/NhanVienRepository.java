package project.an.CoffeeOngBau.Repositories;

import project.an.CoffeeOngBau.Models.Entities.NhanVien;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.sql.Connection;

public class NhanVienRepository implements INhanVienRepository{
    Connection conn;

    public NhanVienRepository() {
        this.conn = DBUtils.openConnection("banhang", "root", "");
    }

    @Override
    public boolean AddNew(NhanVien nv) {
        return true;
    }

    public Connection getConn() {
        return conn;
    }
}
