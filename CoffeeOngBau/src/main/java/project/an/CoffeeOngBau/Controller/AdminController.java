package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.currentAccount;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private Button productAddBtn;

    @FXML
    private Button productCancelBtn;

    @FXML
    private TableColumn<?, ?> productColDonGia;

    @FXML
    private TableColumn<?, ?> productColLoaiSP;

    @FXML
    private TableColumn<?, ?> productColMaSP;

    @FXML
    private TableColumn<?, ?> productColTenSP;

    @FXML
    private TableColumn<?, ?> productColTrangThai;

    @FXML
    private Button productDeleteBtn;

    @FXML
    private AnchorPane productForm;

    @FXML
    private ImageView productImage;

    @FXML
    private Button productImageImport;

    @FXML
    private Button productNavBtn;

    @FXML
    private TableView<?> productTable;

    @FXML
    private Button productUpdateBtn;

    @FXML
    private Button sellNavBtn;

    @FXML
    private Label chucVuText;

    @FXML
    private Label userNameText;

    private Alert alert;

    private String username, chucVu;
    private String[] productCategoriesList;

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
        username = currentAccount.username;
        chucVu = currentAccount.chucVu;
        String user = username + " - " + chucVu;
        userNameText.setText(user);
    }
}
