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
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import static project.an.CoffeeOngBau.Utils.AlertUtils.setAlert;

public class OrderController implements Initializable {
    @FXML
    private TableView<CTHD> orderCTHDTable;

    @FXML
    private TableView<CTHD> orderCTHDWaitingTable;

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
    private TableColumn<HoaDon, String> orderColNVXN;

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
    private Label orderNguoiXNTTxt;

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
    private ObservableList<CTHD> cthds;
    private HashMap<Integer, String> trangThai = new HashMap<>();
    private Alert alert;
    private String sqlSelectWaiting = "SELECT * FROM hoadon WHERE `trangThai` = 1";
    private String sqlSelectOrder = "SELECT * FROM hoadon";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        trangThai.put(0, "");
        getTrangThaiFromDB();
        showOrderWaitingList(sqlSelectWaiting);
        showOrderList(sqlSelectOrder, null);
    }

    public void showOrderList(String sql, ObservableList<HoaDon> hoaDonsL){
        ObservableList<HoaDon> hoaDonList = FXCollections.observableArrayList();
        if(hoaDonsL == null){
            hoaDons = getOrderList(sql);
            hoaDonList.addAll(hoaDons);
        }
        else {
            hoaDonList.addAll(hoaDonsL);
        }
        orderCollMaHD.setCellValueFactory(new PropertyValueFactory<>("maHD"));
        orderColNVTao.setCellValueFactory(new PropertyValueFactory<>("nguoiTao"));
        orderColTGTao.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        orderColTGXacNhan.setCellValueFactory(new PropertyValueFactory<>("confirmedAt"));
        orderColTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        orderColTrangThaiHD.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        orderColGhiChuHD.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        orderColNVXN.setCellValueFactory(new PropertyValueFactory<>("nguoiXacNhan"));
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

        orderTable.setItems(hoaDonList);
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
        orderWaitingTable.setItems(hoaDons);
    }

    public void showSelectOrderWaiting(){
        HoaDon hoaDon = orderWaitingTable.getSelectionModel().getSelectedItem();
        int num = orderWaitingTable.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ) return;
        cthds = getSelecteHD(hoaDon.getMaHD());
        orderColTenSPWaiting.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        orderColDonGiaWaiting.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        orderColSoLuongWaiting.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        orderColThanhTienWaiting.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        orderColGhiChuSPWaiting.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        orderColThanhTienWaiting.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
        orderColDonGiaWaiting.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
        current_data.id = hoaDon.getMaHD();
        System.out.println(current_data.id);
        orderMaHDWaitingTxt.setText(hoaDon.getMaHD());
        orderNVTaoWaitingTxt.setText(hoaDon.getNguoiTao());
        orderTGTaoWaitingTxt.setText(String.valueOf(hoaDon.getCreatedAt()));
        orderLoaiTTWaitingTxt.setText(hoaDon.getThanhToan());
        orderGhiChuWaitingTxt.setText(hoaDon.getGhiChu());
        orderTrangThaiWaitingTxt.setText(hoaDon.getTrangThai());
        orderTongTienSPWaitingTxt.setText(PriceUtils.formatPrice(hoaDon.getTongTien()));
        orderCTHDWaitingTable.setItems(cthds);
    }

    public void showSelectOrder(){
        HoaDon hoaDon = orderTable.getSelectionModel().getSelectedItem();
        int num = orderTable.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ) return;
        cthds = getSelecteHD(hoaDon.getMaHD());
        orderColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        orderColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        orderColSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        orderColThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        orderColGhiChuSP.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        orderColThanhTien.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
        orderColDonGia.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
        current_data.id = hoaDon.getMaHD();
        orderMaHDTxt.setText(hoaDon.getMaHD());
        orderNVTaoTxt.setText(hoaDon.getNguoiTao());
        orderTGTaoTxt.setText(String.valueOf(hoaDon.getCreatedAt()));
        orderTGXacNhanTxt.setText(String.valueOf(hoaDon.getConfirmedAt()));
        orderLoaiTTTxt.setText(hoaDon.getThanhToan());
        orderGhiChuTxt.setText(hoaDon.getGhiChu());
        orderTrangThaiTxt.setText(hoaDon.getTrangThai());
        orderNguoiXNTTxt.setText(hoaDon.getNguoiXacNhan());
        if(hoaDon.getTrangThai().equals("Hủy")) orderTrangThaiTxt.setStyle("-fx-text-fill: #ff0000");
        else if(hoaDon.getTrangThai().equals("Chờ xác nhận")) orderTrangThaiTxt.setStyle("-fx-text-fill: #ffe500");
        else if(hoaDon.getTrangThai().equals("Đã xác nhận")) orderTrangThaiTxt.setStyle("-fx-text-fill: #00ff2b");
        orderTongTienSPTxt.setText(PriceUtils.formatPrice(hoaDon.getTongTien()));
        orderCTHDTable.setItems(cthds);
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
                String tenNVDuyet = "";
                String tenNVXacNhan = "";
                String sqlSelectNhanVienDuyet = "SELECT `tenNV` FROM nhanvien WHERE `maNV` = '" + result.getString("nguoiTao") + "'";
                ResultSet ketqua = conn.prepareStatement(sqlSelectNhanVienDuyet).executeQuery();
                if(ketqua.next()){
                    tenNVDuyet = ketqua.getNString("tenNV");
                }
                String sqlSelectNhanVienXN = "SELECT `tenNV` FROM nhanvien WHERE `maNV` = '" + result.getString("nguoiXacNhan") + "'";
                ResultSet ketqua1 = conn.prepareStatement(sqlSelectNhanVienXN).executeQuery();
                if(ketqua1.next()){
                    tenNVXacNhan = ketqua1.getNString("tenNV");
                    System.out.println(tenNVXacNhan);
                }
                hoaDon = new HoaDon(
                      result.getString("maHD"),
                        tenNVDuyet,
                        result.getString("ghiChu"),
                        result.getString("thanhToan"),
                        trangThai.get(result.getInt("trangThai")),
                        result.getInt("tongTien"),
                        result.getTimestamp("createdAt"),
                        result.getTimestamp("confirmedAt"),
                        tenNVXacNhan
                );
                orderWaitingList.add(hoaDon);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return orderWaitingList;
    }

    private ObservableList<CTHD> getSelecteHD(String maHD){
        ObservableList<CTHD> cthds = FXCollections.observableArrayList();

        String sqlSelectCTHD = "SELECT * FROM cthd WHERE `maHD` = '" + maHD + "'";
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
                cthds.add(cthd);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
        return cthds;
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
            orderTrangThaiFindCBB.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }

    public void confirmOrder(){
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy chọn hóa đơn cần xác nhận!");
        } else {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn xác nhận hóa đơn này?");
            if(optional.get().equals(ButtonType.OK)){
                try{
                    conn = DBUtils.openConnection("banhang", "root", "");
                    System.out.println(current_data.chucVu);
                    String sqlUpdate = "UPDATE `hoadon` SET `trangThai`= 2, `nguoiXacNhan`='"+current_data.userid+"' WHERE `maHD` = '" + current_data.id+"'";
                    prepare = conn.prepareStatement(sqlUpdate);
                    prepare.executeUpdate();
                    DBUtils.closeConnection(conn);
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Xác nhận đơn thành công");
                    showOrderWaitingList(sqlSelectWaiting);
                    showOrderList(sqlSelectOrder, null);
                }
                catch (Exception E){

                }
            }
            else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xác nhận");
            clearWaitingContent();
        }
    }

    public void declineOrder(){
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy chọn hóa đơn cần xác nhận!");
        } else {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn hủy hóa đơn này?");
            if(optional.get().equals(ButtonType.OK)){
                try{
                    conn = DBUtils.openConnection("banhang", "root", "");
                    String sqlDelete = "UPDATE `hoadon` SET `trangThai`= 3, `ghiChu` = '"+orderGhiChuWaitingTxt.getText()+"', `nguoiXacNhan`='"+current_data.userid+"' WHERE `maHD` = '" + current_data.id+"'";
                    prepare = conn.prepareStatement(sqlDelete);
                    prepare.executeUpdate();
                    DBUtils.closeConnection(conn);
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Hủy đơn thành công");
                    showOrderWaitingList(sqlSelectWaiting);
                    showOrderList(sqlSelectOrder, null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xác nhận");
            clearWaitingContent();
        }
    }

    public void redoOrder(){
        if(current_data.id == null){
            setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy chọn hóa đơn cần xác nhận!");
        } else {
            Optional<ButtonType> optional = setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn khôi phục hóa đơn này?");
            if(optional.get().equals(ButtonType.OK)){
                try{
                    conn = DBUtils.openConnection("banhang", "root", "");
                    String sqlDelete = "UPDATE `hoadon` SET `trangThai`= 1, `nguoiXacNhan` = null WHERE `maHD` = '" + current_data.id+"'";
                    prepare = conn.prepareStatement(sqlDelete);
                    prepare.executeUpdate();
                    DBUtils.closeConnection(conn);
                    setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Khôi phục đơn thành công");
                    showOrderWaitingList(sqlSelectWaiting);
                    showOrderList(sqlSelectOrder, null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else setAlert(Alert.AlertType.CONFIRMATION, "Thông tin", "Hủy xác nhậ   n");
            clearContent();
        }
    }

    public void clearWaitingContent(){
        cthds.clear();
        orderWaitingTable.refresh();
        orderMaHDWaitingTxt.setText(null);
        orderNVTaoWaitingTxt.setText(null);
        orderTGTaoWaitingTxt.setText(null);
        orderColThanhTienWaiting.setText(null);
        orderGhiChuWaitingTxt.setText(null);
        orderTrangThaiWaitingTxt.setText(null);
        orderTongTienSPWaitingTxt.setText(null);
    }

    public void clearContent(){
        cthds.clear();
        orderTable.refresh();
        orderMaHDTxt.setText(null);
        orderNVTaoTxt.setText(null);
        orderTGTaoTxt.setText(null);
        orderGhiChuTxt.setText(null);
        orderTrangThaiTxt.setText(null);
        orderTongTienSPTxt.setText(null);
    }

    public void findOrder(){
        String maHDFind = orderMaHDFindTxt.getText().trim();
        String trangThai = orderTrangThaiFindCBB.getValue();
        LocalDate dateStartFind = orderNgayBDFindDP.getValue();
        LocalDate dateEndFind = orderNgayKTFindDP.getValue();
        ObservableList<HoaDon> hoaDonsTemp = FXCollections.observableArrayList();
        hoaDonsTemp.addAll(hoaDons);
        ObservableList<HoaDon> hoaDonsFind = FXCollections.observableArrayList();
        if (maHDFind.isEmpty() && (trangThai == null || trangThai.isEmpty())
                && dateStartFind == null && dateEndFind == null) {
            showOrderList(sqlSelectOrder, null);
            return;
        }
        hoaDonsTemp.stream()
                .filter(hd -> {
                    boolean match = true;

                    // Kiểm tra mã hóa đơn
                    if (!maHDFind.isEmpty()) {
                        match &= hd.getMaHD().contains(maHDFind);
                    }

                    // Kiểm tra trạng thái
                    if (trangThai != null && !trangThai.isEmpty()) {
                        match &= hd.getTrangThai().equalsIgnoreCase(trangThai);
                    }

                    // Kiểm tra ngày tạo
                    if (dateStartFind != null && dateEndFind != null && !dateStartFind.isAfter(dateEndFind)) {
                        LocalDate createdAt = hd.getCreatedAt().toLocalDateTime().toLocalDate();
                        match &= (createdAt.isEqual(dateStartFind) || createdAt.isAfter(dateStartFind)) &&
                                (createdAt.isEqual(dateEndFind) || createdAt.isBefore(dateEndFind));
                    }

                    return match;
                })
                .forEach(hoaDonsFind::add);

        showOrderList(null, hoaDonsFind);
    }

}
