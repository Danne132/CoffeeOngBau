package project.an.CoffeeOngBau.Controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.AlertUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

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
    private Button reportNavBtn;

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

    @FXML
    private AnchorPane orderForm;


    private Alert alert;

    private String username, chucVu;

    ProductController productController;
    List<Button> btns;
    HashMap<String, String> chucvus = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chucvus = DBUtils.getCategoryFromDB("SELECT * FROM chucvunv", "maCV", "tenCV");
        displayName();
        btns = new ArrayList<>(Arrays.asList(employNavBtn, orderNavBtn, productNavBtn, reportNavBtn, sellNavBtn));
        checkQuyen();
        handleLoadReport();
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

    private void checkQuyen(){
        if(current_data.chucVu.equals("TN")){
            employNavBtn.setDisable(true);
            productNavBtn.setDisable(true);
            orderNavBtn.setDisable(true);
        } else if (current_data.chucVu.equals("PC")) {
            sellNavBtn.setDisable(true);
            employNavBtn.setDisable(true);
            productNavBtn.setDisable(true);
        }
    }

    private void displayName(){
        username = current_data.username;
        chucVu = current_data.chucVu;
        String user = username + " - " + chucvus.get(chucVu);
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
            resetColorButton();
            productNavBtn.getStyleClass().add("nav-button-choose");
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
            resetColorButton();
            employNavBtn.getStyleClass().add("nav-button-choose");
            setViewInvisible();
            employeeForm.setVisible(true);
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/employee.fxml"));
            Parent employeeRoot = employeeLoader.load();
            addChildScene(employeeForm,employeeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadSell() {
        try {
            resetColorButton();
            sellNavBtn.getStyleClass().add("nav-button-choose");
            setViewInvisible();
            sellForm.setVisible(true);
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/sell.fxml"));
            Parent employeeRoot = employeeLoader.load();
            addChildScene(sellForm,employeeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadReport() {
        try {
            resetColorButton();
            reportNavBtn.getStyleClass().add("nav-button-choose");
            setViewInvisible();
            reportForm.setVisible(true);
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/report.fxml"));
            Parent employeeRoot = employeeLoader.load();
            addChildScene(reportForm,employeeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadOrder() {
        try {
            resetColorButton();
            orderNavBtn.getStyleClass().add("nav-button-choose");
            setViewInvisible();
            orderForm.setVisible(true);
            FXMLLoader employeeLoader = new FXMLLoader(getClass().getResource("/project/an/CoffeeOngBau/fxml/order.fxml"));
            Parent employeeRoot = employeeLoader.load();
            addChildScene(orderForm,employeeRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewInvisible(){
        productForm.setVisible(false);
        reportForm.setVisible(false);
        employeeForm.setVisible(false);
        sellForm.setVisible(false);
        orderForm.setVisible(false);
    }

    private void resetColorButton(){
        for(Button btn : btns){
            boolean hasStyle = btn.getStyleClass().contains("nav-button-choose");
            if(hasStyle) btn.getStyleClass().remove("nav-button-choose");
        }
    }
}
