package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.currentAccount;
import project.an.CoffeeOngBau.Utils.ComonUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    TextField accInput;
    @FXML
    PasswordField passInput;
    @FXML
    Button loginBtn;
    String account, password;
    Alert alert;
    void initial(){
        account = accInput.getText().toString();
        password = passInput.getText().toString();
    }

    public void Login() throws SQLException {
        initial();
        if (account.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi đăng nhập", "Tên tài khoản và mật khẩu không để trống!");
            return;
        }
        Connection conn = DBUtils.openConnection("banhang", "root", "");

        String sqlSelect = "SELECT * FROM nhanvien";
        Statement lenh = conn.createStatement();
        ResultSet ketQua = lenh.executeQuery(sqlSelect);
        // hiện kết quả
        while(ketQua.next()) {

            if(account.equals(ketQua.getString("username"))&&
                    ComonUtils.hashPassword(password).equals(ketQua.getString("password")))
            {
                currentAccount.username = account;
                currentAccount.chucVu = ketQua.getString("chucVu");
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng nhập thành công!");
                switchToHomeScreen();
            } else
                showAlert(Alert.AlertType.ERROR, "Lỗi đăng nhập", "Tên đăng nhập hoặc mật khẩu không đúng!");
                System.out.println("Đăng nhập không thành công");
        }

        DBUtils.closeConnection(conn);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToHomeScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/admin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Lấy Stage hiện tại
            Stage stage = (Stage) accInput.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Home Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}