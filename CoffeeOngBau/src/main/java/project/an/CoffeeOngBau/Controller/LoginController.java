package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Repositories.NhanVienRepository;
import project.an.CoffeeOngBau.Utils.ComonUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.io.IOException;
import java.sql.*;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;

public class LoginController {
    @FXML
    TextField accInput;
    @FXML
    PasswordField passInput;
    @FXML
    Button loginBtn;
    String account, password;
    NhanVienRepository nhanVienRepository = new NhanVienRepository();
    void initial(){
        account = accInput.getText().toString();
        password = passInput.getText().toString();
    }
    public void Login() throws SQLException {
        initial();
        if(nhanVienRepository.Login(account, password)){
            setAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng nhập thành công!");
            switchToHomeScreen();
        }
        else {
            setAlert(Alert.AlertType.ERROR, "Lỗi đăng nhập", "Tên đăng nhập hoặc mật khẩu không đúng!");
            System.out.println("Đăng nhập không thành công");
        }
    }

    private void switchToHomeScreen() {
        try {
            StackPane stackPane = new StackPane();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/admin.fxml"));
            fxmlLoader.setRoot(stackPane);
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
