package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.an.CoffeeOngBau.Utils.ComonUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    @FXML
    TextField accInput, passInput;
    @FXML
    Button loginBtn;
    String account, password;
    void initial(){
        account = accInput.getText().toString();
        password = passInput.getText().toString();
    }

    public void Login() throws SQLException {
        Connection conn = DBUtils.openConnection("banhang", "root", "");

        String sqlSelect = "SELECT * FROM nhanvien";
        Statement lenh = conn.createStatement();
        ResultSet ketQua = lenh.executeQuery(sqlSelect);
        // hiện kết quả
        while(ketQua.next()) {

            if(account.equals(ketQua.getString("account"))&&
                    ComonUtils.hashPassword(password).equals(ketQua.getString("password")) ) {
                DBUtils.closeConnection(conn);

            }
        }

        DBUtils.closeConnection(conn);
    }

}
