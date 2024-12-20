package project.an.CoffeeOngBau.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import project.an.CoffeeOngBau.Models.Entities.CTHD;
import project.an.CoffeeOngBau.Models.Entities.SanPham;
import project.an.CoffeeOngBau.Utils.DBUtils;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class SellController implements Initializable {
    @FXML
    private TableView<CTHD> orderTable;

    @FXML
    private AnchorPane productForm;

    @FXML
    private TableColumn<CTHD, String> sellColDonGia;

    @FXML
    private TableColumn<CTHD, String> sellColGhiChu;

    @FXML
    private TableColumn<CTHD, String> sellColSoLuong;

    @FXML
    private TableColumn<CTHD, String> sellColTenSP;

    @FXML
    private TableColumn<CTHD, String> sellColThanhTien;

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
    private ComboBox<?> sellThanhToanCBB;

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
    private ObservableList<CTHD> cthdList = FXCollections.observableArrayList();;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sellDisplayCard();
        showCTHDList();
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
                cardP.setData(cardList.get(i));
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
        for(CTHD ct : cthds){
            cthdList.add(ct);
        }
        sellColTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        sellColDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        sellColSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        sellColGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        sellColThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        orderTable.setItems(cthdList);
    }
}