package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class SellController {
    @FXML
    private TableView<?> orderTable;

    @FXML
    private AnchorPane productForm;

    @FXML
    private TableColumn<?, ?> sellColDonGia;

    @FXML
    private TableColumn<?, ?> sellColGhiChu;

    @FXML
    private TableColumn<?, ?> sellColSoLuong;

    @FXML
    private TableColumn<?, ?> sellColTenSP;

    @FXML
    private TableColumn<?, ?> sellColThanhTien;

    @FXML
    private Button sellHuyBtn;

    @FXML
    private TextField sellKhachTraText;

    @FXML
    private ScrollPane sellScrollPane;

    @FXML
    private Button sellThanhToanBtn;

    @FXML
    private ComboBox<?> sellThanhToanCBB;

    @FXML
    private TextField sellTienThuaText;

    @FXML
    private TextField sellTongTienHDText;

    @FXML
    private TextField sellTongTienSPText;


}
