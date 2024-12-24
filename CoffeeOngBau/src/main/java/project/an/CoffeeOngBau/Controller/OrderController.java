package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import project.an.CoffeeOngBau.Models.Entities.CTHD;
import project.an.CoffeeOngBau.Models.Entities.HoaDon;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.ResourceBundle;

public class OrderController implements Initializable {
    @FXML
    private TableView<CTHD> orderCTHDTable;

    @FXML
    private TableView<CTHD> orderCTHDWaitingTable;

    @FXML
    private Label orderChiPhiKhacTxt;

    @FXML
    private Label orderChiPhiKhacWaitingTxt;

    @FXML
    private TableColumn<CTHD, Integer> orderColDonGia;

    @FXML
    private TableColumn<CTHD, Integer> orderColDonGiaWaiting;

    @FXML
    private TableColumn<HoaDon, String> orderColGhiChuHD;

    @FXML
    private TableColumn<HoaDon, String> orderColGhiChuHDWaiting;

    @FXML
    private TableColumn<CTHD, String> orderColGhiChuSP;

    @FXML
    private TableColumn<CTHD, String> orderColGhiChuSPWaiting;

    @FXML
    private TableColumn<HoaDon, String> orderColNVTao;

    @FXML
    private TableColumn<HoaDon, String> orderColNVTaoWaiting;

    @FXML
    private TableColumn<CTHD, Integer> orderColSoLuong;

    @FXML
    private TableColumn<CTHD, Integer> orderColSoLuongWaiting;

    @FXML
    private TableColumn<HoaDon, Timestamp> orderColTGTao;

    @FXML
    private TableColumn<HoaDon, Timestamp> orderColTGTaoWaiting;

    @FXML
    private TableColumn<HoaDon, Timestamp> orderColTGXacNhan;

    @FXML
    private TableColumn<CTHD, String> orderColTenSP;

    @FXML
    private TableColumn<CTHD, String> orderColTenSPWaiting;

    @FXML
    private TableColumn<CTHD, Integer> orderColThanhTienWaiting;

    @FXML
    private TableColumn<CTHD, Integer> orderColThanhTien;

    @FXML
    private TableColumn<HoaDon, Integer> orderColTongTien;

    @FXML
    private TableColumn<HoaDon, Integer> orderColTongTienWaiting;

    @FXML
    private TableColumn<HoaDon, String> orderColTrangThaiHD;

    @FXML
    private TableColumn<HoaDon, String> orderCollMaHD;

    @FXML
    private TableColumn<HoaDon, String> orderCollMaHDWaiting;

    @FXML
    private Button orderFindBtn;

    @FXML
    private AnchorPane orderForm;

    @FXML
    private TextArea orderGhiChuTxt;

    @FXML
    private TextArea orderGhiChuWaitingTxt;

    @FXML
    private Button orderHuyDonBtn;

    @FXML
    private Button orderKhoiPhucBtn;

    @FXML
    private Label orderLoaiTTTxt;

    @FXML
    private Label orderLoaiTTWaitingTxt;

    @FXML
    private TextField orderMaHDFindTxt;

    @FXML
    private Label orderMaHDTxt;

    @FXML
    private Label orderMaHDWaitingTxt;

    @FXML
    private Label orderNVTaoTxt;

    @FXML
    private Label orderNVTaoWaitingTxt;

    @FXML
    private DatePicker orderNgayBDFindDP;

    @FXML
    private DatePicker orderNgayKTFindDP;

    @FXML
    private Label orderTGTaoTxt;

    @FXML
    private Label orderTGTaoWaitingTxt;

    @FXML
    private Label orderTGXacNhanTxt;

    @FXML
    private TableView<HoaDon> orderTable;

    @FXML
    private Label orderTongHoaDonTxt;

    @FXML
    private Label orderTongTienHDTxt;

    @FXML
    private Label orderTongTienSPTxt;

    @FXML
    private Label orderTongTienSPWaitingTxt;

    @FXML
    private ComboBox<String> orderTrangThaiFindCBB;

    @FXML
    private Label orderTrangThaiTxt;

    @FXML
    private Label orderTrangThaiWaitingTxt;

    @FXML
    private TableView<HoaDon> orderWaitingTable;

    @FXML
    private Button orderXacNhanBtn;

    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private ObservableList<HoaDon> hoaDons;
    private HashMap<Integer, String> trangThai = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTrangThaiFromDB();
        showOrderWaitingList("SELECT * FROM hoadon WHERE `trangThai` = 1");
        showOrderList("SELECT * FROM hoadon");
    }

    public void showOrderList(String sql){
        hoaDons = getOrderList(sql);
        orderCollMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        orderColNVTao.setCellValueFactory(new PropertyValueFactory<>("nguoiTao"));
        orderColTGTao.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        orderColTGXacNhan.setCellValueFactory(new PropertyValueFactory<>("confirmedAt"));
        orderColTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        orderColTrangThaiHD.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        orderColGhiChuHD.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        orderColTongTien.setCellFactory(tc -> new TableCell<HoaDon, Integer>() {
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

        orderWaitingTable.setItems(hoaDons);
    }

    public void showOrderWaitingList(String sql){
        hoaDons = getOrderList(sql);
        orderCollMaHDWaiting.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        orderColNVTaoWaiting.setCellValueFactory(new PropertyValueFactory<>("nguoiTao"));
        orderColTGTaoWaiting.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        orderColTongTienWaiting.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        orderColGhiChuHDWaiting.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        orderColTongTienWaiting.setCellFactory(tc -> new TableCell<HoaDon, Integer>() {
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

        orderTable.setItems(hoaDons);
    }

    private ObservableList<HoaDon> getOrderList(String sql){
        ObservableList<HoaDon> orderWaitingList = FXCollections.observableArrayList();
        String sqlSelectOrderWaiting = sql;
        try {
            conn = DBUtils.openConnection("banhang", "root", "");
            prepare = conn.prepareStatement(sqlSelectOrderWaiting);
            result = prepare.executeQuery(sqlSelectOrderWaiting);
            HoaDon hoaDon;
            while(result.next()){
                String tenNV = "";
                String sqlSelectNhanVien = "SELECT `tenNV` FROM nhanvien WHERE `maNV` = '" + result.getString("nguoiTao") + "'";
                ResultSet ketqua = conn.prepareStatement(sqlSelectNhanVien).executeQuery();
                if(ketqua.next()){
                    tenNV = ketqua.getNString("tenNV");
                }
                hoaDon = new HoaDon(
                      result.getString("maHD"),
                        tenNV,
                        result.getString("ghiChu"),
                        result.getString("thanhToan"),
                        trangThai.get(result.getInt("trangThai")),
                        result.getInt("tongTien"),
                        result.getTimestamp("createdAt"),
                        result.getTimestamp("confirmedAt")
                );
                orderWaitingList.add(hoaDon);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return orderWaitingList;
    }

    private void selecteHD(){
        HoaDon hoaDon = orderTable.getSelectionModel().getSelectedItem();
        int num = orderTable.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ) return;
        String sqlSelectCTHD = "SELECT * FROM cthd WHERE `maHD` = '" + hoaDon.getMaHD() + "'";
        conn = DBUtils.openConnection("banhang", "root", "");
        try {
            prepare = conn.prepareStatement(sqlSelectCTHD);
            result = prepare.executeQuery();
            while(result.next()){
                String tenSP = "";
                String sqlSelectSP = "SELECT `tenSP` FROM sanpham WHERE `maSP` = '" + result.getString("maSP") + "'";
                ResultSet ketqua = conn.prepareStatement(sqlSelectSP).executeQuery();
                if(ketqua.next()) tenSP = ketqua.getNString("tenSP");
                CTHD cthd = new CTHD(
                        result.getNString("maSP"),
                        tenSP,
                        result.getNString("ghiChu"),
                        result.getInt("donGia"),
                        result.getInt("soLuong")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void getTrangThaiFromDB() {
        conn = DBUtils.openConnection("banhang", "root", "");
        String sqlSelect = "SELECT * FROM trangthaihd";
        Statement lenh = null;
        try {
            lenh = conn.createStatement();
            ResultSet ketQua = lenh.executeQuery(sqlSelect);
            while(ketQua.next()){
                int maLoai = ketQua.getInt("maTrangThai");
                String tenLoai = ketQua.getString("tenTrangThai");
                trangThai.put(maLoai, tenLoai);
            }
            ObservableList list = FXCollections.observableArrayList(trangThai.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }
}
