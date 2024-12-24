package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import project.an.CoffeeOngBau.Utils.DBUtils;
import project.an.CoffeeOngBau.Utils.PriceUtils;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    private BarChart<?, ?> reportDonHangChart;

    @FXML
    private AnchorPane reportForm;

    @FXML
    private AreaChart<?, ?> reportThuNhapChart;

    @FXML
    private Label reportThuNhapTodayTxt;

    @FXML
    private Label reportTongDonTodayTxt;

    @FXML
    private Label reportTongDonTxt;

    @FXML
    private Label reportTongThuNhapTxt;

    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTongDon();
        displayThuNhapHomNay();
    }

    public void displayTongDon(){
        String sql = "SELECT COUNT(`maHD`) FROM hoadon";
        conn = DBUtils.openConnection("banhang", "root", "");
        try{
            int tongDon = 0;
            prepare = conn.prepareStatement(sql);
            result = prepare.executeQuery();
            if(result.next()){
                tongDon = result.getInt("COUNT(`maHD`)");
                reportTongDonTxt.setText(String.valueOf(tongDon));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    public void displayThuNhapHomNay(){
        LocalDate today = LocalDate.now();
        String sql = "SELECT SUM(`tongTien`) FROM hoadon WHERE DATE(`createdAt`) = '"+today+"' AND `trangThai` = 2";
        conn = DBUtils.openConnection("banhang", "root", "");
        try{
            int tongTienHN = 0;
            prepare = conn.prepareStatement(sql);
            result = prepare.executeQuery();
            if(result.next()){
                tongTienHN = result.getInt("SUM(`tongTien`)");
                reportThuNhapTodayTxt.setText(PriceUtils.formatPrice(tongTienHN));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            DBUtils.closeConnection(conn);
        }
    }
}
