package project.an.CoffeeOngBau.Controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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


    private ObservableList<SanPham> cardList = FXCollections.observableArrayList();
    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    public static List<CTHD> cthds = new ArrayList<>();
    private ObservableList<CTHD> cthdList = FXCollections.observableArrayList();
    private String[] loaiTT = new String[]{"Trả tiền mặt", "Chuyển khoản"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sellDisplayCard();
        showCTHDList();
        getLoaiTT();
    }

    public ObservableList<SanPham> sellGetData(){
        String sqlSelect = "SELECT * FROM sanpham WHERE `trangThai`=1";
        ObservableList<SanPham> listData = FXCollections.observableArrayList();
        conn = DBUtils.openConnection("banhang", "root", "");
        try{
            prepare = conn.prepareStatement(sqlSelect);
            result = prepare.executeQuery();
            SanPham sp;
            while(result.next()){
                sp = new SanPham(
                        result.getString("maSP"),
                        result.getNString("tenSP"),
                        result.getString("anhSP"),
                        result.getInt("donGia")
                );
                listData.add(sp);
            }
        } catch (Exception e){
        }
        DBUtils.closeConnection(conn);
        return listData;
    }

    public void sellDisplayCard(){
        cardList.clear();
        cardList.addAll(sellGetData());
        int row = 0;
        int column =0;
        sellGridPane.getRowConstraints().clear();
        sellGridPane.getColumnConstraints().clear();
        for(int i = 0; i < cardList.size(); i++){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/project/an/CoffeeOngBau/fxml/card_product.fxml"));
                AnchorPane pane = loader.load();
                CardProductController cardP = loader.getController();
                cardP.setData(cardList.get(i), this);
                if(column == 3){
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
    }
}