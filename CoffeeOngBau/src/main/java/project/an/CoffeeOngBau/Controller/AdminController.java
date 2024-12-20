package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.current_data;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminController implements Initializable {
    @FXML
    private AnchorPane adminForm;

    @FXML
    private Button employNavBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private Button orderNavBtn;

    @FXML
    private Button productNavBtn;

    @FXML
    private Button sellNavBtn;

    @FXML
    private Label userNameText;

    @FXML
    private AnchorPane productForm;

    @FXML
    private AnchorPane employeeForm;

    @FXML
    private AnchorPane reportForm;

    @FXML
    private AnchorPane sellForm;


    private Alert alert;

    private String username, chucVu;

    ProductController productController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();
    }

    @FXML
    public void logout(){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc muốn đăng xuất?");
        Optional<ButtonType> option = alert.showAndWait();

        if(option.get().equals(ButtonType.OK)){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/login.fxml"));
            Scene scene = null;
            logOutBtn.getScene().getWindow().hide();
            try {
                scene = new Scene(fxmlLoader.load());
                // Lấy Stage hiện tại
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Login Form");
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void displayName(){
        username = current_data.username;
        chucVu = current_data.chucVu;
        String user = username + " - " + chucVu;
        userNameText.setText(user);
    }


    public void addChildScene(AnchorPane form ,Parent childRoot) {
        form.getChildren().clear(); // xóa tất cả các con hiện tại
        form.getChildren().add(childRoot);
        AnchorPane.setTopAnchor(childRoot, 0.0);
        AnchorPane.setBottomAnchor(childRoot, 0.0);
        AnchorPane.setLeftAnchor(childRoot, 0.0);
        AnchorPane.setRightAnchor(childRoot, 0.0);
    }

    @FXML
    private void handleLoadProduct() {
        try {
            setViewInvisible();
            productForm.setVisible(true);
            FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/product.fxml"));
            Parent productRoot = productLoader.load();
            addChildScene(productForm,productRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadEmployee() {
        try {
            setViewInvisible();
            employeeForm.setVisible(true);
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/employee.fxml"));
            Parent employeeRoot = employeeLoader.load();
            addChildScene(employeeForm,employeeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewInvisible(){
        productForm.setVisible(false);
        reportForm.setVisible(false);
        employeeForm.setVisible(false);
        sellForm.setVisible(false);
    }
}
