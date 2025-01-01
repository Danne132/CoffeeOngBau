package project.an.CoffeeOngBau.Controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import project.an.CoffeeOngBau.Models.Entities.CTHD;
import project.an.CoffeeOngBau.Models.Entities.HoaDon;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Models.Entities.current_data;
import project.an.CoffeeOngBau.Repositories.SanPhamRespository;
import project.an.CoffeeOngBau.Utils.AlertUtils;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SellController implements Initializable {
    @FXML
    private TableView<CTHD> orderTable;

    @FXML
    private AnchorPane productForm;

    @FXML
    private TableColumn<CTHD, Integer> sellColDonGia;

    @FXML
    private TableColumn<CTHD, String> sellColGhiChu;

    @FXML
    private TableColumn<CTHD, Integer> sellColSoLuong;

    @FXML
    private TableColumn<CTHD, String> sellColTenSP;

    @FXML
    private TableColumn<CTHD, Integer> sellColThanhTien;

    @FXML
    private GridPane sellGridPane;

    @FXML
    private Button sellHuyBtn;

    @FXML
    private TextField sellKhachTraText;

    @FXML
    private ScrollPane sellScrollPane;

    @FXML
    private Button sellThanhToanBtn;

    @FXML
    private ComboBox<String> sellThanhToanCBB;

    @FXML
    private TextField sellTienThuaText;

    @FXML
    private TextField sellTongTienHDText;

    @FXML
    private TextField sellTongTienSPText;

    @FXML
    private TextField sellTenSPText;

    @FXML
    private ComboBox<String> sellLoaiSPCBB;

    private HashMap<String, String> loaisps = new HashMap<>();
    private ObservableList<SanPham> cardList = FXCollections.observableArrayList();
    private ObservableList<SanPham> cardListFilter = FXCollections.observableArrayList();
    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    public static List<CTHD> cthds = new ArrayList<>();
    private ObservableList<CTHD> cthdList = FXCollections.observableArrayList();
    private String[] loaiTT = new String[]{"Trả tiền mặt", "Chuyển khoản"};
    private String maHD;
    private SanPhamRespository sanPhamRespository = new SanPhamRespository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loaisps = sanPhamRespository.getLoaiSPS();
        ObservableList list = FXCollections.observableArrayList(loaisps.values());
        sellLoaiSPCBB.setItems(list);
        cardList = sellGetDataFromDB();
        sellDisplayCard(cardList);
        showCTHDList();
        getLoaiTT();
//        getCategoryFromDB();
    }

    public ObservableList<SanPham> sellGetDataFromDB(){
        String sqlSelect = "SELECT * FROM sanpham WHERE `trangThai`=1";
        ObservableList<SanPham> listData = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        try{
            prepare = conn.prepareStatement(sqlSelect);
            result = prepare.executeQuery();
            SanPham sp;
            while(result.next()){
                String maLoaiSP = loaisps.get(result.getString("loaiSP"));
                sp = new SanPham(
                        result.getString("maSP"),
                        result.getNString("tenSP"),
                        result.getString("anhSP"),
                        result.getInt("donGia"),
                        maLoaiSP
                );
                listData.add(sp);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        DBUtils.closeConnection(conn);
        return listData;
    }



    public void sellDisplayCard(ObservableList<SanPham> observableList){
        cardListFilter.clear();
        cardListFilter.addAll(observableList);
        int row = 0;
        int column =0;
        sellGridPane.getRowConstraints().clear();
        sellGridPane.getColumnConstraints().clear();
        for(int i = 0; i < cardListFilter.size(); i++){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/project/an/CoffeeOngBau/fxml/card_product.fxml"));
                AnchorPane pane = loader.load();
                CardProductController cardP = loader.getController();
                cardP.setData(cardListFilter.get(i), this);
                if(column == 4){
                    column = 0;
                    row++;
                }
                sellGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void showCTHDList(){
        cthdList.clear();
        for(CTHD ct : cthds){
            cthdList.add(ct);
        }
        setHDInfor();
        sellColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        sellColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        sellColSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        sellColGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        sellColThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        orderTable.setItems(cthdList);

        sellColGhiChu.setCellFactory(TextFieldTableCell.forTableColumn());
        sellColGhiChu.setOnEditCommit(event -> {
            CTHD cthd = event.getRowValue();
            cthd.setGhiChu(event.getNewValue());
        });

        sellColSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sellColSoLuong.setOnEditCommit(event -> {
            CTHD cthd = event.getRowValue();
            cthd.setSoLuong(event.getNewValue());
            if (cthd.getSoLuong() == 0){
                cthds.remove(cthd);
                cthdList.remove(cthd);
                System.out.println("Đã xóa sản phẩm này");
            }
            setHDInfor();
            orderTable.refresh();

        });

        sellColThanhTien.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
        sellColDonGia.setCellFactory(tc -> new TableCell<CTHD, Integer>() {
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
    }

    public void clearTable(){
        orderTable.getItems().clear();
    }

    public void setHDInfor(){
        int tongTien = 0;
        for(CTHD cthd : cthds){
            tongTien += cthd.getThanhTien();
        }
        sellTongTienHDText.setText(String.valueOf(tongTien));
        sellTongTienSPText.setText(String.valueOf(tongTien));
    }

    public void tinhTienThua() {
        String input = sellKhachTraText.getText();
        int tongTien = 0;
        for(CTHD cthd : cthds){
            tongTien += cthd.getThanhTien();
        }
        int tienKhachTra = Integer.parseInt(input) - tongTien;
        sellTienThuaText.setText(String.valueOf(tienKhachTra));
    }

    private void getLoaiTT(){
        List<String> listTT = new ArrayList<>();
        for(String tt : loaiTT){
            listTT.add(tt);
        }
        ObservableList list = FXCollections.observableArrayList(listTT);
        sellThanhToanCBB.setItems(list);
    }

    public void clearHD(){
        cthds.clear();
        cthdList.clear();
        orderTable.getItems().clear();
        sellTongTienHDText.setText(null);
        sellTongTienSPText.setText(null);
        sellThanhToanCBB.getSelectionModel().select(null);
        sellKhachTraText.setText(null);
        sellTienThuaText.setText(null);
        sellLoaiSPCBB.getSelectionModel().select(null);
        sellTenSPText.setText(null);
        sellDisplayCard(sellGetDataFromDB());
    }

    public void taoHD(){
        if(sellKhachTraText.getText().isEmpty()||sellThanhToanCBB.getValue().isEmpty()){
            AlertUtils.setAlert(Alert.AlertType.ERROR, "Lỗi", "Hãy điền đủ thông tin hóa đơn");
        }
        else {
            Optional<ButtonType> btn = AlertUtils.setAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", "Bạn muốn tạo hóa đơn nay?");
            if(btn.get().equals(ButtonType.OK)){
                maHD = taoMaHD();
                String maNV = current_data.userid;
                System.out.println(maHD);
                conn = DBUtils.openConnection("banhang", "root", "");
                String sqlInsertHD = "INSERT INTO `hoadon`(`maHD`, `nguoiTao`, `tongTien`,`thanhToan`, `ghiChu`) VALUES (?,?,?,?,?)";
                try{
                    prepare = conn.prepareStatement(sqlInsertHD);
                    prepare.setString(1, maHD);
                    prepare.setString(2, maNV);
                    prepare.setInt(3, Integer.parseInt(sellTongTienHDText.getText()));
                    prepare.setString(4, sellThanhToanCBB.getValue());
                    prepare.setString(5, "");
                    int rowsInserted = prepare.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm hóa đơn thành công");
                    } else {
                        System.out.println("Không thể thêm hóa đơn");
                    }
                    taoCTHD(maHD);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBUtils.closeConnection(conn);
                    clearHD();
                }
            }
            else AlertUtils.setAlert(Alert.AlertType.INFORMATION, "Thông tin", "Hủy tạo hóa đơn");
        }

    }

    private String taoMaHD(){
        String maHD = "HD";
        LocalDate currentDate = LocalDate.now();
        System.out.println(currentDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String ngayTao = currentDate.format(formatter);
        maHD += ngayTao;
        String sqlSelect = "SELECT * from hoadon WHERE DATE(`createdAt`) = ? ORDER BY `maHD` DESC LIMIT 1";
        conn = DBUtils.openConnection("banhang", "root", "");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlSelect)) {
            preparedStatement.setString(1,  String.valueOf(currentDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return maHD + "0001";
                }
                String lastHD = resultSet.getString("maHD");
                System.out.println("Mã lớn nhất: " + lastHD);
                int number = Integer.parseInt(lastHD.substring(maHD.length()));
                return maHD + String.format("%04d", number + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }


    private void taoCTHD(String maHD) throws SQLException {
        String sqlInsertCTHD = "INSERT INTO `cthd`(`maHD`, `maSP`, `donGia` ,`soLuong`, `thanhTien`, `ghiChu`) VALUES (?,?,?,?,?,?)";
        for(CTHD cthd : cthds){
            prepare = conn.prepareStatement(sqlInsertCTHD);
            prepare.setString(1, maHD);
            prepare.setString(2, cthd.getMaSP());
            prepare.setInt(3, cthd.getDonGia());
            prepare.setInt(4, cthd.getSoLuong());
            prepare.setInt(5, cthd.getThanhTien());
            prepare.setString(6, cthd.getGhiChu());
            prepare.executeUpdate();
        }
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
            sellLoaiSPCBB.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBUtils.closeConnection(conn);
    }

    public void findSPSell(){
        ObservableList<SanPham> filteredList = FXCollections.observableArrayList();
        filteredList.clear();
        String searchName = sellTenSPText.getText().toLowerCase();
        String selectedLoaiSP = sellLoaiSPCBB.getValue();
        for (SanPham sp : cardList) {
            if (sp.getTenSP().toLowerCase().contains(searchName) &&
                    (selectedLoaiSP == null || sp.getLoaiSP().equals(selectedLoaiSP))) {
                filteredList.add(sp);
            }
        }
        sellGridPane.getChildren().clear();
        sellDisplayCard(filteredList);
    }
}