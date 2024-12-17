package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Models.Entities.LoaiSP;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.currentAccount;
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
    private Button productAddBtn;

    @FXML
    private Button productCancelBtn;

    @FXML
    private Button productCategoryAddBtn;

    @FXML
    private TableColumn<SanPham, String> productColDonGia;

    @FXML
    private TableColumn<SanPham, String> productColLoaiSP;

    @FXML
    private TableColumn<SanPham, String> productColMaSP;

    @FXML
    private TableColumn<SanPham, String> productColTenSP;

    @FXML
    private TableColumn<SanPham, String> productColTrangThai;

    @FXML
    private Button productDeleteBtn;

    @FXML
    private TextField productDonGiaText;

    @FXML
    private AnchorPane productForm;

    @FXML
    private TextArea productGhiChuText;

    @FXML
    private ImageView productImage;

    @FXML
    private Button productImageImport;

    @FXML
    private ComboBox<String> productLoaiSPCBB;

    @FXML
    private TextField productMaSPText;

    @FXML
    private TextArea productMoTaText;

    @FXML
    private Button productNavBtn;

    @FXML
    private TableView<SanPham> productTable;

    @FXML
    private TextField productTenSPText;

    @FXML
    private ComboBox<String> productTrangThaiCBB;

    @FXML
    private Button productUpdateBtn;

    @FXML
    private Button sellNavBtn;

    @FXML
    private Label userNameText;

    private Alert alert;

    private String username, chucVu;
    private HashMap<String, String> loaisps = new HashMap<>();
    private String[] trangthaisps = new String[]{"Đang bán", "Ngưng bán"};
    private Connection conn;
    private ObservableList<SanPham> sanPhams;
    private Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();
        getCategoryFromDB();
        getProductStatus();
        showSPList();
    }

    public void showSPList(){
        sanPhams = getSPList();
        productColMaSP.setCellValueFactory(new PropertyValueFactory<>("maSP"));
        productColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        productColLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSP"));
        productColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        productColTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        productTable.setItems(sanPhams);
    }

    public ObservableList<SanPham> getSPList(){
        ObservableList<SanPham> spList = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM sanpham";
        Statement lenh = null;
        try {
            lenh = conn.createStatement();
            ResultSet ketQua = lenh.executeQuery(sqlSelect);
            SanPham sp;
            while(ketQua.next()){
                sp = new SanPham(
                        ketQua.getString("maSP"),
                        ketQua.getString("tenSP"),
                        ketQua.getString("loaiSP"),
                        ketQua.getString("moTa"),
                        ketQua.getString("ghiChu"),
                        ketQua.getString("trangThai"),
                        ketQua.getString("anhSP"),
                        ketQua.getDouble("donGia")
                );
                spList.add(sp);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return spList;
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

    private void getCategoryFromDB()  {
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM loaisp";
        Statement lenh = null;
        try {
            lenh = conn.createStatement();
            ResultSet ketQua = lenh.executeQuery(sqlSelect);
            while(ketQua.next()){
                String maLoai = ketQua.getString("maLoai");
                String tenLoai = ketQua.getString("tenLoai");
                loaisps.put(maLoai, tenLoai);
            }
            ObservableList list = FXCollections.observableArrayList(loaisps.values());
            productLoaiSPCBB.setItems(list);
            productLoaiSPCBB.getSelectionModel().select(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }

    private void getProductStatus(){
        List<String> listTT = new ArrayList<>();
        for(String trangthai : trangthaisps){
            listTT.add(trangthai);
        }
        ObservableList list = FXCollections.observableArrayList(listTT);
        productTrangThaiCBB.setItems(list);
        productTrangThaiCBB.getSelectionModel().select(0);
    }

    public void cleanSP(){
        productMaSPText.setText("");
        productTenSPText.setText("");
        productDonGiaText.setText("");
        productMoTaText.setText("");
        productGhiChuText.setText("");
        productImage.setImage(null);
    }

    public void addSP(){
        if(productTenSPText.getText().isEmpty() ||
                productDonGiaText.getText().isEmpty()||
                currentAccount.path == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi thêm sản phẩm");
            alert.setHeaderText(null);
            alert.setContentText("Hãy điền đủ thông tin sản phẩm");
            alert.showAndWait();
        } else {
            String maSP = setAutoMaSP();
            System.out.println(maSP);
            conn = DBUtils.openConnection("banhang", "root", "");
            String sqlInsert = "INSERT INTO sanpham "
                    + "(maSP, tenSP, loaiSP, donGia, anhSP, moTa, ghiChu, trangThai) "
                    + "VALUES(?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement lenh = conn.prepareStatement(sqlInsert);
                lenh.setString(1, maSP);
                lenh.setString(2, productTenSPText.getText());
                String getMaLoai = "";
                for(String key : loaisps.keySet()){
                    if(loaisps.get(key) == productLoaiSPCBB.getSelectionModel().getSelectedItem()){
                        getMaLoai = key;
                        break;
                    }
                }
                lenh.setString(3, getMaLoai);
                lenh.setDouble(4, Double.parseDouble(productDonGiaText.getText()));
                String path = currentAccount.path;
                path = path.replace("\\", "\\\\");
                lenh.setString(5, path);
                lenh.setString(6, productMoTaText.getText());
                String tt = productTrangThaiCBB.getSelectionModel().getSelectedItem();
                if(tt == "Đang bán")
                    lenh.setBoolean(7, true);
                else
                    lenh.setBoolean(7, false);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thêm sản phẩm");
                alert.setHeaderText(null);
                alert.setContentText("Thêm sản phẩm thành công");
                alert.showAndWait();
                DBUtils.closeConnection(conn);
                showSPList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void importImage(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(adminForm.getScene().getWindow());
        if(file != null){
            currentAccount.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 113, 125, false, true);
            productImage.setImage(image);
        }
    }

    private String setAutoMaSP(){
        String getMaLoai = "";
        for(String key : loaisps.keySet()){
            if(loaisps.get(key) == productLoaiSPCBB.getSelectionModel().getSelectedItem()){
                getMaLoai = key;
                break;
            }
        }
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT maSP FROM sanpham WHERE loaiSP = ? ORDER BY maSP DESC LIMIT 1";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, getMaLoai);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String lastMaSP = resultSet.getString("maSP");
                    int number = Integer.parseInt(lastMaSP.substring(getMaLoai.length()));
                    DBUtils.closeConnection(conn);
                    return getMaLoai + String.format("%03d", number + 1);
                } else {
                    DBUtils.closeConnection(conn);
                    return getMaLoai + "001";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
