package project.an.CoffeeOngBau.Models.Entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class NhanVien {
    String id, tenNV, chucVu, SDT, email, username, password, anhNV, isWorking;
    Timestamp createAt, updatedAt;
    boolean gioiTinh;
    Date ngaySinh;

    public NhanVien(String id, String tenNV, String chucVu, String SDT, String email, String username, String password, String anhNV, String isWorking, Timestamp createAt, Timestamp updatedAt, boolean gioiTinh, Date ngaySinh) {
        this.id = id;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.SDT = SDT;
        this.email = email;
        this.username = username;
        this.password = password;
        this.anhNV = anhNV;
        this.isWorking = isWorking;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public NhanVien(String id, String tenNV, String chucVu, String SDT, String email, String username, String password, String isWorking, String anhNV, boolean gioiTinh, Date ngaySinh) {
        this.id = id;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.SDT = SDT;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isWorking = isWorking;
        this.anhNV = anhNV;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public String getIsWorking() {
        return isWorking;
    }

    public void setIsWorking(String isWorking) {
        this.isWorking = isWorking;
    }

    public String getAnhNV() {
        return anhNV;
    }

    public void setAnhNV(String anhNV) {
        this.anhNV = anhNV;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
