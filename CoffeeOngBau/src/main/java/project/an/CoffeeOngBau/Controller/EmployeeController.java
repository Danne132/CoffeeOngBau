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
import javafx.stage.FileChooser;
import javafx.util.converter.LocalDateStringConverter;
import project.an.CoffeeOngBau.Models.Entities.NhanVien;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.ComonUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private ComboBox<String> employeeChucVuCBB;

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
    private ComboBox<String> employeeTrangThaiCBB;

    @FXML
    private Button employeeUpdateBtn;

    @FXML
    private ComboBox<String> loaiNVFindCBB;

    @FXML
    private TextField tenNVFindText;

    @FXML
    private ComboBox<String> trangThaiNVFindCBB;

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
        if(employeeTenNVText.getText().isEmpty() ||
                (!employeeNamRd.isSelected() && !employeeNuRd.isSelected())||
                employeeNgaySinhDP.getValue()==null|| employeeEmailText.getText().isEmpty()||
                employeeSDTText.getText().isEmpty()||
                current_data.path == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin nhân viên!");
        } else {
            String maNV = setAutoMaNV();
            String maChucVu = "";
            for(String key : chucvunvs.keySet()){
                if(chucvunvs.get(key) == employeeChucVuCBB.getSelectionModel().getSelectedItem()){
                    maChucVu = key;
                    break;
                }
            }
            String tt = employeeTrangThaiCBB.getSelectionModel().getSelectedItem();
            String path = current_data.path;
            path = path.replace("\\", "\\\\");
            conn = DBUtils.openConnection("banhang", "root", "");
            String sqlInsert = "INSERT INTO `nhanvien` (`maNV`, `tenNV`, `gioiTinh`, `ngaySinh`, `chucVu`, `SDT`, `email`, `anhNV`, `isWorking`, `username`, `password`) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement lenh = conn.prepareStatement(sqlInsert);
                lenh.setString(1, maNV);
                lenh.setString(2, employeeTenNVText.getText());
                lenh.setBoolean(3, setGender());
                lenh.setDate(4, Date.valueOf(employeeNgaySinhDP.getValue()));
                lenh.setString(5, maChucVu);
                lenh.setString(6, employeeSDTText.getText());
                lenh.setString(7, employeeEmailText.getText());
                lenh.setString(8, path);
                if(tt == "Đang làm")
                    lenh.setBoolean(9, true);
                else
                    lenh.setBoolean(9, false);
                lenh.setString(10, employeeEmailText.getText());
                lenh.setString(11, ComonUtils.hashPassword(employeeSDTText.getText()));
                setAlert(Alert.AlertType.INFORMATION, "Thêm", "Thêm nhân viên thành công");
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
            reloadNV();
            showSPList("SELECT * FROM nhanvien");
        }
    }

    public void updateNV() {
        if(employeeTenNVText.getText().isEmpty() ||
                (!employeeNamRd.isSelected() && !employeeNuRd.isSelected())||
                employeeNgaySinhDP.getValue()==null|| employeeEmailText.getText().isEmpty()||
                employeeSDTText.getText().isEmpty()||
                current_data.path == null||current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin!");
        } else {
            String cv = getMaCV(employeeChucVuCBB);
            System.out.println(cv);
            int isWork;
            if(employeeTrangThaiCBB.getSelectionModel().getSelectedItem().equals("Đang làm")) isWork = 1;
            else isWork = 0;
            int gender = setGender()?1:0;
            String path = current_data.path;
            path = path.replace("\\", "\\\\");
            String sqlUpdate = "UPDATE `nhanvien` SET" +
                    "`tenNV`='"
                    +employeeTenNVText.getText()+"',`gioiTinh`='"
                    +gender+
                    "',`ngaySinh`='"+Date.valueOf(employeeNgaySinhDP.getValue())+"',`chucVu`='"
                    +cv+"',`SDT`='"+employeeSDTText.getText()+"',`email`='"
                    +employeeEmailText.getText()+"',`anhNV`='"
                    +path+"',`isWorking`='"+isWork+"' WHERE `maNV`='"+current_data.id+"'";
            conn = DBUtils.openConnection("banhang", "root", "");
            try {
                Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn có chắc muốn cập nhật thông tin của nhân viên " + employeeTenNVText.getText() + "?");
                if(optional.get().equals(ButtonType.OK)){
                    prepare = conn.prepareStatement(sqlUpdate);
                    prepare.executeUpdate();
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Cập nhật thông tin thành công!");
                    showSPList("SELECT * FROM nhanvien");
                    reloadNV();
                } else {
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã hủy cập nhật!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void deleteNV() {
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy chọn nhân viên cần xóa!");
        } else {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn xóa thông tin nhân viên này?");
            if(optional.get().equals(ButtonType.OK)){
                try {
                    String sqlDelete = "DELETE FROM `nhanvien` WHERE `maNV`='"+current_data.id+"'";
                    conn = DBUtils.openConnection("banhang", "root", "");
                    prepare = conn.prepareStatement(sqlDelete);
                    prepare.executeUpdate();
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Đã xóa thông tin nhân viên này!");
                    DBUtils.closeConnection(conn);
                    reloadNV();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xóa");
        }
    }

    public void reloadNV() {
        employeeMaNVText.setText("");
        employeeTenNVText.setText("");
        employeeNamRd.setSelected(false);
        employeeNuRd.setSelected(false);
        employeeNgaySinhDP.setValue(null);
        employeeEmailText.setText("");
        employeeSDTText.setText("");
        employeeChucVuCBB.setValue(null);
        employeeTrangThaiCBB.setValue(null);
        employeeImage.setImage(null);
        tenNVFindText.setText("");
        loaiNVFindCBB.setValue(null);
        trangThaiNVFindCBB.setValue(null);
        current_data.id = "";
        showSPList("SELECT * FROM nhanvien");
    }

    public void importImage(){
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(employeeForm.getScene().getWindow());
        if(file != null){
            current_data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 113, 125, false, true);
            employeeImage.setImage(image);
        }
    }

    public void selectNV() {
        NhanVien nhanVien = employeeTable.getSelectionModel().getSelectedItem();
        int num = employeeTable.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ) return;
        employeeMaNVText.setText(nhanVien.getId());
        employeeTenNVText.setText(nhanVien.getTenNV());
        if(nhanVien.isGioiTinh()){
            employeeNamRd.setSelected(true);
            employeeNuRd.setSelected(false);
        }
        else {
            employeeNuRd.setSelected(true);
            employeeNamRd.setSelected(false);
        }
        String getDate = String.valueOf(nhanVien.getNgaySinh());
        LocalDate parse = LocalDate.parse(getDate);
        System.out.println(parse);
        employeeNgaySinhDP.setValue(parse);
        employeeEmailText.setText(nhanVien.getEmail());
        employeeSDTText.setText(nhanVien.getSDT());
        current_data.path = nhanVien.getAnhNV();
        current_data.id = nhanVien.getId();
        System.out.println(current_data.path);
        String path = "File:"+ current_data.path;
        Image image = new Image(path, 113, 125, false, true);
        employeeImage.setImage(image);
        String cvNV= nhanVien.getChucVu();
        System.out.println(cvNV);
        employeeChucVuCBB.getSelectionModel().select(cvNV);
        employeeTrangThaiCBB.getSelectionModel().select(nhanVien.getIsWorking()=="Đang làm"?0:1);
    }

    public void findNV() {
        String sqlFind;
        String maCV = getMaCV(loaiNVFindCBB);
        String trangThai = trangThaiNVFindCBB.getValue()==null?"NULL":trangThaiNVFindCBB.getValue()=="Đang bán"?"1":"0";
        String tenNV = tenNVFindText.getText();
        maCV = maCV==null?"NULL":"'"+maCV+"'";
        System.out.println("mã loại"+maCV);
        System.out.println("trạng thái"+trangThai);
        sqlFind = "SELECT * " +
                "FROM nhanvien " +
                "WHERE " +
                "    (`tenNV` LIKE '%"+tenNV+"%'OR'"+tenNV+"' IS NULL OR '"+tenNV+"' = '') AND " +
                "    (`chucvu` = "+maCV+" OR "+maCV+" IS NULL) AND" +
                "    (`isWorking` = "+trangThai+" OR "+trangThai+" IS NULL);";
        showSPList(sqlFind);
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
                        result.getBoolean("isWorking")?"Đang làm":"Nghỉ làm",
                        result.getTimestamp("createdAt"),
                        result.getTimestamp("updatedAt"),
                        result.getBoolean("gioiTinh"),
                        result.getDate("ngaySinh")
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

    public boolean setGender(){
        if(employeeNamRd.isSelected()){
            employeeNuRd.setSelected(false);
            return true;
        }
        else{
            employeeNamRd.setSelected(false);
            return false;
        }
    }

    private String setAutoMaNV(){
        String getChucVu = getMaCV(employeeChucVuCBB);
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT maNV FROM nhanvien WHERE chucVu LIKE ? ORDER BY maNV DESC LIMIT 1";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1, getChucVu + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return getChucVu + "001";
                }
                String lastMaNV = resultSet.getString("maNV");
                System.out.println("Mã lớn nhất: " + lastMaNV);
                int number = Integer.parseInt(lastMaNV.substring(getChucVu.length()));
                return getChucVu + String.format("%03d", number + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    private String getMaCV(ComboBox<?> cbb) {
        String maLoai = null;
        for(String key : chucvunvs.keySet()){
            if(chucvunvs.get(key) == cbb.getSelectionModel().getSelectedItem()){
                maLoai = key;
                return maLoai;
            }
        }
        System.out.println(maLoai);
        return maLoai;
    }

}
