package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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

//    private void loadProductView(){
//        productForm.setVisible(true);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/product.fxml"));
//        try {
//            Node productView = loader.load();
//            productController = loader.getController();
//            if (productController != null) {
//                System.out.println("ProductController được khởi tạo thành công!");
//            } else {
//                System.out.println("ProductController chưa được khởi tạo!");
//            }
//            productForm.getChildren().setAll(productView);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public void addChildScene(Parent childRoot) {
        productForm.getChildren().clear(); // xóa tất cả các con hiện tại
        productForm.getChildren().add(childRoot);
    }

    @FXML
    private void handleLoadProduct() {
        try {
            productForm.setVisible(true);
            FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/product.fxml"));
            Parent productRoot = productLoader.load();
            addChildScene(productRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
