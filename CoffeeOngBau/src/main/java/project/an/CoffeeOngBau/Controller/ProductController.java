package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import project.an.CoffeeOngBau.Models.Entities.CTHD;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Repositories.SanPhamRespository;
import project.an.CoffeeOngBau.Utils.AlertUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.ImportImageUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;

public class ProductController implements Initializable {
    @FXML
    private Button productAddBtn;

    @FXML
    private Button productCancelBtn;

    @FXML
    private Button productCategoryAddBtn;

    @FXML
    private TableColumn<SanPham, Integer> productColDonGia;

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
    private TableView<SanPham> productTable;

    @FXML
    private TextField productTenSPText;

    @FXML
    private ComboBox<String> productTrangThaiCBB;

    @FXML
    private Button productUpdateBtn;

    @FXML
    private TextField tenSPFindText;

    @FXML
    private ComboBox<String> trangThaiSPFindCBB;

    @FXML
    private ComboBox<String> loaiSPFindCBB;

    @FXML
    private Button spFindBTN;

    private Alert alert;


    private SanPhamRespository sanPhamRespository = new SanPhamRespository();
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
        getCategoryFromDB();
        getProductStatus();
        showSPList();
    }

    public void showSPList(){
//        sanPhams = getSPList(sql);
        sanPhams = sanPhamRespository.getAllSP(loaisps);
        productColMaSP.setCellValueFactory(new PropertyValueFactory<>("maSP"));
        productColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        productColLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSP"));
        productColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        productColTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        productColDonGia.setCellFactory(tc -> new TableCell<SanPham, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(PriceUtils.formatPrice(item));
                }
            }
        });
        productTable.setItems(sanPhams);
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
        showSPList();
    }

    public void addSP(){
        if(productTenSPText.getText().isEmpty() ||
                productDonGiaText.getText().isEmpty()||
                current_data.path == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin sản phẩm!");
        } else {
            SanPham sp = getDataFromUI();
            sanPhamRespository.addSP(sp);
            reloadSP();
            showSPList();
        }
    }

    private SanPham getDataFromUI(){
        String maSP = setAutoMaSP();
        String tenSP = productTenSPText.getText();
        String getMaLoai = "";
        for(String key : loaisps.keySet()){
            if(loaisps.get(key) == productLoaiSPCBB.getSelectionModel().getSelectedItem()){
                getMaLoai = key;
                break;
            }
        }
        int donGia = Integer.parseInt(productDonGiaText.getText());
        String path = current_data.path;
        path = path.replace("\\", "\\\\");
        String moTa = productMoTaText.getText();
        String ghiChu = productGhiChuText.getText();
        String trangThai = productTrangThaiCBB.getSelectionModel().getSelectedItem();
        return new SanPham(maSP, tenSP, getMaLoai, moTa, ghiChu, trangThai, path, donGia);
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
        System.out.println(current_data.path);
        String path = "File:"+ current_data.path;
        Image image = new Image(path, 113, 125, false, true);
        productImage.setImage(image);
        String loaiSP = sanPham.getLoaiSP();
        System.out.println(loaiSP);
        productLoaiSPCBB.getSelectionModel().select(loaiSP);
        productTrangThaiCBB.getSelectionModel().select(sanPham.getTrangThai()=="Đang bán"?0:1);
    }

    public void updateSP(){
        if(productTenSPText.getText().isEmpty() ||
                productDonGiaText.getText().isEmpty()||
                current_data.path == null||current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin sản phẩm!");
        } else {
            SanPham sp = getDataFromUI();
            sanPhamRespository.updateSP(sp);
            showSPList();
            reloadSP();
        }
    }

    public void deleteSP(){
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy chọn sản phẩm cần xóa!");
        } else {
            sanPhamRespository.deleteSP();
            showSPList();
            reloadSP();
        }

    }

    public void importImage(){
        image = ImportImageUtils.getImageFromUser(productForm);
        productImage.setImage(image);
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
        showSPList();
    }

    private String setAutoMaSP(){
        String getMaLoai = getMaLoai(productLoaiSPCBB);
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT maSP FROM sanpham WHERE loaiSP LIKE ? ORDER BY maSP DESC LIMIT 1";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, getMaLoai + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return getMaLoai + "001";
                }
                String lastMaNV = resultSet.getString("maSP");
                System.out.println("Mã lớn nhất: " + lastMaNV);
                int number = Integer.parseInt(lastMaNV.substring(getMaLoai.length()));
                return getMaLoai + String.format("%03d", number + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
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
