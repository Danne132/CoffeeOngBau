package project.an.CoffeeOngBau.Models.Entities;

import java.sql.Timestamp;

public class NhanVien {
    String id, tenNV, chucVu, SDT, email, username, password, idCardNumber;
    Timestamp createAt, updatedAt;

    public NhanVien(String id, String tenNV, String chucVu, String SDT, String email, String username, String password, String idCardNumber, Timestamp createAt, Timestamp updatedAt) {
        this.id = id;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.SDT = SDT;
        this.email = email;
        this.username = username;
        this.password = password;
        this.idCardNumber = idCardNumber;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
