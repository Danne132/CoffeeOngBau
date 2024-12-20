package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import project.an.CoffeeOngBau.Models.Entities.NhanVien;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class EmployeeController implements Initializable {

    @FXML
    private Button NVFindBTN;

    @FXML
    private Button employeeAddBtn;

    @FXML
    private Button employeeCancelBtn;

    @FXML
    private Button employeeCategoryAddBtn;

    @FXML
    private ComboBox<?> employeeChucVuCBB;

    @FXML
    private TableColumn<NhanVien, String> employeeColLoaiNV;

    @FXML
    private TableColumn<NhanVien, String> employeeColMaNV;

    @FXML
    private TableColumn<NhanVien, String> employeeColNgay;

    @FXML
    private TableColumn<NhanVien, String> employeeColTenNV;

    @FXML
    private TableColumn<NhanVien, String> employeeColTrangThai;

    @FXML
    private Button employeeDeleteBtn;

    @FXML
    private TextField employeeEmailText;

    @FXML
    private AnchorPane employeeForm;

    @FXML
    private ImageView employeeImage;

    @FXML
    private Button employeeImageImport;

    @FXML
    private TextField employeeMaNVText;

    @FXML
    private RadioButton employeeNamRd;

    @FXML
    private DatePicker employeeNgaySinhDP;

    @FXML
    private RadioButton employeeNuRd;

    @FXML
    private TextField employeeSDTText;

    @FXML
    private TableView<NhanVien> employeeTable;

    @FXML
    private TextField employeeTenNVText;

    @FXML
    private ComboBox<?> employeeTrangThaiCBB;

    @FXML
    private Button employeeUpdateBtn;

    @FXML
    private ComboBox<?> loaiNVFindCBB;

    @FXML
    private TextField tenNVFindText;

    @FXML
    private ComboBox<?> trangThaiNVFindCBB;

    private Alert alert;

    private HashMap<String, String> chucvunvs = new HashMap<>();
    private String[] trangthainvs = new String[]{"Đang làm", "Nghỉ làm"};
    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private ObservableList<NhanVien> nhanViens;
    private Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCategoryFromDB();
        getEmployeeStatus();
        showSPList("SELECT * FROM nhanvien");
    }

    public void addNV(ActionEvent event) {
    }

    public void updateNV(ActionEvent event) {
    }
    
    public void deleteNV(ActionEvent event) {
    }

    public void reloadNV(ActionEvent event) {
    }
    
    public void importImage(ActionEvent event) {
    }

    public void selecteNV(MouseEvent mouseEvent) {
    }

    public void findNV(ActionEvent event) {
    }

    private Optional<ButtonType> setAlert(Alert.AlertType alertType, String title, String message){
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(message);
        return alert.showAndWait();
    }

    private void getCategoryFromDB()  {
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM chucvunv";
        statement = null;
        try {
            statement = conn.createStatement();
            ResultSet ketQua = statement.executeQuery(sqlSelect);
            System.out.println(ketQua==null?"Chưa lấy được":"Đã lấy được");
            while(ketQua.next()){
                String maCV = ketQua.getString("maCV");
                String tenCV = ketQua.getString("tenCV");
                System.out.println(maCV +"   "+tenCV);
                chucvunvs.put(maCV, tenCV);
            }
            ObservableList list = FXCollections.observableArrayList(chucvunvs.values());
            employeeChucVuCBB.setItems(list);
            employeeChucVuCBB.getSelectionModel().select(0);
            loaiNVFindCBB.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }

    private void getEmployeeStatus(){
        List<String> listTT = new ArrayList<>();
        for(String trangthai : trangthainvs){
            listTT.add(trangthai);
        }
        ObservableList list = FXCollections.observableArrayList(listTT);
        employeeTrangThaiCBB.setItems(list);
        employeeTrangThaiCBB.getSelectionModel().select(0);
        trangThaiNVFindCBB.setItems(list);
    }

    public void showSPList(String sql){
        nhanViens = getSPList(sql);
        employeeColMaNV.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        employeeColLoaiNV.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        employeeColTrangThai.setCellValueFactory(new PropertyValueFactory<>("isWorking"));
        employeeColNgay.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        employeeTable.setItems(nhanViens);
    }

    public ObservableList<NhanVien> getSPList(String sql){
        ObservableList<NhanVien> nvList = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = sql;
        try {
            prepare = conn.prepareStatement(sqlSelect);
            result = prepare.executeQuery(sqlSelect);
            NhanVien nhanVien;
            while(result.next()){
                nhanVien = new NhanVien(
                        result.getString("maNV"),
                        result.getNString("tenNV"),
                        chucvunvs.get(result.getString("chucVu")),
                        result.getNString("SDT"),
                        result.getNString("email"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("anhNV"),
                        result.getBoolean("gioiTinh"),
                        result.getTimestamp("createdAt"),
                        result.getTimestamp("updatedAt"),
                        result.getBoolean("isWorking")?"Đang làm":"Nghỉ làm"
                );
                nvList.add(nhanVien);
                nhanVien.getChucVu();
            }
            employeeTable.setItems(nvList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return nvList;
    }
}
