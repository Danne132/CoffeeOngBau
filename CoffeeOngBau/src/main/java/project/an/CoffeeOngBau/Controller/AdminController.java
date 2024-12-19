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

    @FXML
    private TextField tenSPFindText;

    @FXML
    private ComboBox<String> trangThaiSPFindCBB;

    @FXML
    private ComboBox<String> loaiSPFindCBB;

    @FXML
    private Button spFindBTN;

    private Alert alert;

    private String username, chucVu;
    private HashMap<String, String> loaisps = new HashMap<>();
    private String[] trangthaisps = new String[]{"Đang bán", "Ngưng bán"};
    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private ObservableList<SanPham> sanPhams;
    private Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayName();
        getCategoryFromDB();
        getProductStatus();
        showSPList("SELECT * FROM sanpham");
    }

    public void showSPList(String sql){
        sanPhams = getSPList(sql);
        productColMaSP.setCellValueFactory(new PropertyValueFactory<>("maSP"));
        productColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        productColLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSP"));
        productColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        productColTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        productTable.setItems(sanPhams);
    }

    public ObservableList<SanPham> getSPList(String sql){
        ObservableList<SanPham> spList = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = sql;
        try {
            prepare = conn.prepareStatement(sqlSelect);
            result = prepare.executeQuery(sqlSelect);
            SanPham sp;
            while(result.next()){
                sp = new SanPham(
                        result.getString("maSP"),
                        result.getNString("tenSP"),
                        loaisps.get(result.getString("loaiSP")),
                        result.getNString("moTa"),
                        result.getNString("ghiChu"),
                        result.getBoolean("trangThai")?"Đang bán":"Ngừng bán",
                        result.getString("anhSP"),
                        result.getDouble("donGia")
                );
                spList.add(sp);
            }
            productTable.setItems(spList);
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
        username = current_data.username;
        chucVu = current_data.chucVu;
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
            loaiSPFindCBB.setItems(list);
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
        trangThaiSPFindCBB.setItems(list);
    }

    public void reloadSP(){
        productMaSPText.setText("");
        productTenSPText.setText("");
        productDonGiaText.setText("");
        productMoTaText.setText("");
        productGhiChuText.setText("");
        productImage.setImage(null);
        tenSPFindText.setText(null);
        loaiSPFindCBB.setValue(null);
        trangThaiSPFindCBB.setValue(null);
        current_data.id = "";
        showSPList("SELECT * FROM sanpham");
    }

    public void addSP(){
        if(productTenSPText.getText().isEmpty() ||
                productDonGiaText.getText().isEmpty()||
                current_data.path == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin sản phẩm!");
        } else {
            String maSP = setAutoMaSP();
            System.out.println(maSP);
            conn = DBUtils.openConnection("banhang", "root", "");
            String sqlInsert = "INSERT INTO `sanpham` (`maSP`, `tenSP`, `loaiSP`, `donGia`, `anhSP`, `moTa`, `ghiChu`, `trangThai`) VALUES(?,?,?,?,?,?,?,?)";
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
                String path = current_data.path;
                path = path.replace("\\", "\\\\");
                lenh.setString(5, path);
                lenh.setString(6, productMoTaText.getText());
                lenh.setString(7, productGhiChuText.getText());
                String tt = productTrangThaiCBB.getSelectionModel().getSelectedItem();
                if(tt == "Đang bán")
                    lenh.setBoolean(8, true);
                else
                    lenh.setBoolean(8, false);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thêm sản phẩm");
                alert.setHeaderText(null);
                alert.setContentText("Thêm sản phẩm thành công");
                alert.showAndWait();
                int rowsInserted = lenh.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Thêm sản phẩm thành công");
                } else {
                    System.out.println("Không thể thêm sản phẩm.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            DBUtils.closeConnection(conn);
            reloadSP();
            showSPList("SELECT * FROM sanpham");
        }
    }

    public void selecteSP(){
        SanPham sanPham = productTable.getSelectionModel().getSelectedItem();
        int num = productTable.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ) return;
        productMaSPText.setText(sanPham.getMaSP());
        productTenSPText.setText(sanPham.getTenSP());
        productDonGiaText.setText(String.valueOf(sanPham.getDonGia()));
        productMoTaText.setText(sanPham.getMoTa());
        productGhiChuText.setText(sanPham.getGhiChu());
        current_data.path = sanPham.getAnhSP();
        current_data.id = sanPham.getMaSP();
        String path = "File:"+ current_data.path;
        Image image = new Image(path, 113, 125, false, true);
        productImage.setImage(image);
        String loaiSP = null;
        for(String key : loaisps.keySet()){
            if(loaisps.get(key) == sanPham.getLoaiSP()){
                loaiSP = key;
                break;
            }
        }
        productLoaiSPCBB.getSelectionModel().select(loaiSP);
        productTrangThaiCBB.getSelectionModel().select(sanPham.getTrangThai()=="Đang bán"?0:1);
    }

    public void updateSP(){
        if(productTenSPText.getText().isEmpty() ||
                productDonGiaText.getText().isEmpty()||
                current_data.path == null||current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin sản phẩm!");
        } else {
            String maLoai = getMaLoai(productLoaiSPCBB);

            int isSell;
            if(productTrangThaiCBB.getSelectionModel().getSelectedItem().equals("Đang bán")) isSell = 1;
            else isSell = 0;
            String sqlUpdate = "UPDATE `sanpham` SET" +
                    "`maSP`='"+productMaSPText.getText()+"',`tenSP`='"
                    +productTenSPText.getText()+"',`loaiSP`='"
                    +maLoai+
                    "',`donGia`='"+productDonGiaText.getText()+"',`anhSP`='"
                    +current_data.path+"',`moTa`='"+productMoTaText.getText()+"',`ghiChu`='"
                    +productGhiChuText.getText()+"',`trangThai`='"
                    +isSell+"' WHERE `maSP`='"+current_data.id+"'";
            conn = DBUtils.openConnection("banhang", "root", "");
            try {
                Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn có chắc muốn cập nhật thông tin của mặt hàng " + productMaSPText.getText() + "?");
                if(optional.get().equals(ButtonType.OK)){
                    prepare = conn.prepareStatement(sqlUpdate);
                    prepare.executeUpdate();
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Cập nhật thông tin thành công!");
                    showSPList("SELECT * FROM sanpham");
                    reloadSP();
                } else {
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã hủy cập nhật!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteSP(){
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin sản phẩm!");
        } else {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn xóa sản phẩm này?");
            if(optional.get().equals(ButtonType.OK)){
                try {
                    String sqlDelete = "DELETE FROM `sanpham` WHERE `maSP`='"+current_data.id+"'";
                    conn = DBUtils.openConnection("banhang", "root", "");
                    prepare = conn.prepareStatement(sqlDelete);
                    prepare.executeUpdate();
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã xóa sản phẩm này!");
                    DBUtils.closeConnection(conn);
                    showSPList("SELECT * FROM sanpham");
                    reloadSP();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xóa sản phẩm");
        }
    }

    public void importImage(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(adminForm.getScene().getWindow());
        if(file != null){
            current_data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 113, 125, false, true);
            productImage.setImage(image);
        }
    }

    public void findSP(){
        String sqlFind;
        String maLoai = getMaLoai(loaiSPFindCBB);
        String trangThai = trangThaiSPFindCBB.getValue()==null?"NULL":trangThaiSPFindCBB.getValue()=="Đang bán"?"1":"0";
        String tenSP = tenSPFindText.getText();
        maLoai = maLoai==null?"NULL":"'"+maLoai+"'";
        System.out.println("mã loại"+maLoai);
        System.out.println("trạng thái"+trangThai);
        sqlFind = "SELECT * " +
                "FROM sanpham " +
                "WHERE " +
                "    (`tenSP` LIKE '%"+tenSP+"%'OR'"+tenSP+"' IS NULL OR '"+tenSP+"' = '') AND " +
                "    (`loaiSP` = "+maLoai+" OR "+maLoai+" IS NULL) AND" +
                "    (`trangThai` = "+trangThai+" OR "+trangThai+" IS NULL);";
        showSPList(sqlFind);
    }

    private String setAutoMaSP(){
        String getMaLoai = getMaLoai(productLoaiSPCBB);
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM `sanpham` WHERE `maSP` LIKE '"+getMaLoai+"%'";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
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

    private Optional<ButtonType> setAlert(Alert.AlertType alertType, String title, String message){
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(message);
        return alert.showAndWait();
    }

    private String getMaLoai(ComboBox<String> cbb){
        String maLoai = null;
        for(String key : loaisps.keySet()){
            if(loaisps.get(key) == cbb.getSelectionModel().getSelectedItem()){
                maLoai = key;
                return maLoai;
            }
        }
        System.out.println(maLoai);
        return maLoai;
    }

}
