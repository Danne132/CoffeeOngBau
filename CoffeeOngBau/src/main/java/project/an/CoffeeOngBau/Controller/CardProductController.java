package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import project.an.CoffeeOngBau.Models.Entities.CTHD;
import project.an.CoffeeOngBau.Models.Entities.HoaDon;
import project.an.CoffeeOngBau.Models.Entities.SanPham;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CardProductController implements Initializable {

    @FXML
    private AnchorPane cardForm;

    @FXML
    private ImageView cardProductImage;

    @FXML
    private Label cardProductName;

    @FXML
    private Label cardProductPrice;

    private SanPham sanPham;
    private Image image;
    private Connection conn;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private SellController sell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(SanPham sanPham, SellController sell){
        this.sell = sell;
        this.sanPham = sanPham;
        cardProductName.setText(sanPham.getTenSP());
        cardProductPrice.setText(String.valueOf(sanPham.getDonGia())+" đ");
        String path = "File:"+ sanPham.getAnhSP();
        image = new Image(path, 124, 124, false, true);
        cardProductImage.setImage(image);
    }



    public void addCTHD(MouseEvent mouseEvent) {
        boolean exists = false;

        // Kiểm tra danh sách xem mã sản phẩm đã tồn tại hay chưa
        for (CTHD cthd : SellController.cthds) {
            if (sanPham.getMaSP().equals(cthd.getMaSP())) {
                exists = true; // Đánh dấu là đã tồn tại
                break; // Thoát khỏi vòng lặp vì không cần kiểm tra thêm
            }
        }

        // Nếu mã sản phẩm chưa tồn tại, thêm sản phẩm vào danh sách
        if (!exists) {
            SellController.cthds.add(new CTHD(sanPham.getMaSP(), sanPham.getTenSP(), "", sanPham.getDonGia(), 1));
            sell.clearTable();
            sell.showCTHDList();
        }
    }
}
