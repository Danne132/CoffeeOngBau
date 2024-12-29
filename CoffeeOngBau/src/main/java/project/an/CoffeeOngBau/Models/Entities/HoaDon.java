package project.an.CoffeeOngBau.Models.Entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    List<CTHD> cthds;
    String maHD, nguoiTao, ghiChu, thanhToan, trangThai, nguoiXacNhan;
    int tongTien;
    Timestamp createdAt;
    Timestamp confirmedAt;

    public HoaDon(String maHD, String nguoiTao, String ghiChu, String thanhToan, String trangThai, int tongTien, Timestamp createdAt, Timestamp confirmedAt, String nguoiXacNhan) {
        this.maHD = maHD;
        this.nguoiTao = nguoiTao;
        this.ghiChu = ghiChu;
        this.thanhToan = thanhToan;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.createdAt = createdAt;
        this.confirmedAt = confirmedAt;
        this.nguoiXacNhan = nguoiXacNhan;
    }

    public List<CTHD> getCthds() {
        return cthds;
    }

    public void setCthds(List<CTHD> cthds) {
        this.cthds = cthds;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(String nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(String thanhToan) {
        this.thanhToan = thanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getConfirmedAt() {
        return confirmedAt;
    }

    public String getNguoiXacNhan() {
        return nguoiXacNhan;
    }

    public void setNguoiXacNhan(String nguoiXacNhan) {
        this.nguoiXacNhan = nguoiXacNhan;
    }
}
