package project.an.CoffeeOngBau.Models.Entities;

import javafx.beans.property.IntegerProperty;

public class CTHD {
    String tenSP, ghiChu, maSP;
    int donGia, soLuong, thanhTien;

    public CTHD(String maSP,String tenSP, String ghiChu, int donGia, int soLuong) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.ghiChu = ghiChu;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.thanhTien = soLuong * donGia;
    }

    public String getMaSP() {
        return maSP;
    }

    private void updateThanhTien() {
        this.thanhTien = this.soLuong * this.donGia;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        updateThanhTien();
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int donGia, int soLuong) {
        this.thanhTien = donGia*soLuong;
    }
}
