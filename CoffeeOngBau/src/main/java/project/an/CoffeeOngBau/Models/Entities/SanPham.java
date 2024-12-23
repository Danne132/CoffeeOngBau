package project.an.CoffeeOngBau.Models.Entities;

public class SanPham {
    private String maSP, tenSP, loaiSP, moTa, ghiChu, anhSP, trangThai;
    private int donGia;

    public SanPham(String maSP, String tenSP, String loaiSP, String moTa, String ghiChu, String trangThai, String anhSP, int donGia) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.moTa = moTa;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
        this.anhSP = anhSP;
        this.donGia = donGia;
    }

    public SanPham(String maSP, String tenSP, String anhSP, int donGia, String loaiSP){
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.anhSP = anhSP;
        this.donGia = donGia;
        this.loaiSP = loaiSP;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getAnhSP() {
        return anhSP;
    }

    public void setAnhSP(String anhSP) {
        this.anhSP = anhSP;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }
}
