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
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import project.an.CoffeeOngBau.Models.Entities.NhanVien;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Repositories.NhanVienRepository;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.ImportImageUtils;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;

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

    private NhanVienRepository nhanVienRepository = new NhanVienRepository();
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
        nhanViens = nhanVienRepository.getAllNVList();
//        chucvunvs.put("", null);
        chucvunvs.putAll(nhanVienRepository.getLoainvs());
        setupCBB();
        showNVList(nhanViens);
    }

    public void showNVList(ObservableList<NhanVien> nvs){
        ObservableList<NhanVien> nhanViensTemp = nvs;
        employeeColMaNV.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        employeeColLoaiNV.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        employeeColTrangThai.setCellValueFactory(new PropertyValueFactory<>("isWorking"));
        employeeColNgay.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        employeeTable.setItems(nhanViensTemp);
    }

    public void addNV(ActionEvent event) {
        if(employeeTenNVText.getText().isEmpty() ||
                (!employeeNamRd.isSelected() && !employeeNuRd.isSelected())||
                employeeNgaySinhDP.getValue()==null|| employeeEmailText.getText().isEmpty()||
                employeeSDTText.getText().isEmpty()||
                current_data.path == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin nhân viên!");
        } else {
            String maNV = nhanVienRepository.setAutoMaNV(employeeChucVuCBB);
            String tenNV = employeeTenNVText.getText();
            boolean gioiTinh = setGender();
            Date ngaySinh = Date.valueOf(employeeNgaySinhDP.getValue());
            String maChucVu = nhanVienRepository.getMaCV(employeeChucVuCBB);
            String sdt = employeeSDTText.getText();
            String email = employeeEmailText.getText();
            String path = current_data.path;
            path = path.replace("\\", "\\\\");
            String isWorking = employeeTrangThaiCBB.getSelectionModel().getSelectedItem();
            NhanVien nv = new NhanVien(maNV, tenNV, maChucVu, sdt, email, email, sdt, isWorking, path, gioiTinh, ngaySinh);
            nhanVienRepository.addNV(nv);
            reloadNV();
            showNVList(nhanVienRepository.getAllNVList());
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
            String cv = nhanVienRepository.getMaCV(employeeChucVuCBB);
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
                    for(NhanVien nv : nhanViens){
                        if(nv.getId().equals(current_data.id)) {
                            nv.setIsWorking(isWork == 1 ? "Đang làm" : "Nghỉ làm");
                            nv.setChucVu(chucvunvs.get(cv));
                            nv.setTenNV(employeeTenNVText.getText());
                            break;
                        }
                    }
                    showNVList(nhanVienRepository.getAllNVList());
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
            nhanVienRepository.deleteNV();
            reloadNV();
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
        showNVList(nhanVienRepository.getAllNVList());
    }

    public void importImage(){
        image = ImportImageUtils.getImageFromUser(employeeForm);
        employeeImage.setImage(image);
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
        String path = "File:"+ current_data.path;
        Image image = new Image(path, 113, 125, false, true);
        employeeImage.setImage(image);
        String cvNV= nhanVien.getChucVu();
        employeeChucVuCBB.getSelectionModel().select(cvNV);
        employeeTrangThaiCBB.getSelectionModel().select(nhanVien.getIsWorking()=="Đang làm"?0:1);
    }

    public void findNV() {
        showNVList(nhanVienRepository.getAllNVList());
        String maLoai = loaiNVFindCBB.getValue();
        String trangThai = trangThaiNVFindCBB.getValue();
        String tenNV = tenNVFindText.getText();
        ObservableList<NhanVien> nhanViensFind = FXCollections.observableArrayList();
        if (tenNV.isEmpty() && (trangThai == null || trangThai.isEmpty() && (maLoai == null || maLoai.isEmpty()))) {
            showNVList(nhanVienRepository.getAllNVList());
            return;
        }

        for (NhanVien nv : nhanViens) {
            if (nv.getTenNV().toLowerCase().contains(tenNV.toLowerCase()) &&
                    (maLoai == null || nv.getChucVu().equals(maLoai)) && (trangThai == null || nv.getIsWorking().equals(trangThai))) {
                nhanViensFind.add(nv);
            }
        }
        showNVList(nhanViensFind);
    }

    private void setupCBB(){
        ObservableList list = FXCollections.observableArrayList(chucvunvs.values());
        loaiNVFindCBB.setItems(list);
        employeeChucVuCBB.setItems(list);
        List<String> listTT = new ArrayList<>();
        for(String trangthai : trangthainvs){
            listTT.add(trangthai);
        }
        ObservableList listTrangThai = FXCollections.observableArrayList(listTT);
        employeeTrangThaiCBB.setItems(listTrangThai);
        employeeTrangThaiCBB.getSelectionModel().select(0);
        trangThaiNVFindCBB.setItems(listTrangThai);
    }

    public boolean setGender(){
        if(employeeNamRd.isSelected()){
            employeeNuRd.setSelected(false);
            return true;
        }
        if(employeeNuRd.isSelected()){
            employeeNamRd.setSelected(false);
            return false;
        }
        return false;
    }
}
