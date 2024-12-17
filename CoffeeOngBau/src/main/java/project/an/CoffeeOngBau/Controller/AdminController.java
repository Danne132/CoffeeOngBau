package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class AdminController {
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
}
