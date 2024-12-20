package project.an.CoffeeOngBau.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import project.an.CoffeeOngBau.Models.Entities.SanPham;

import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(SanPham sanPham){
        this.sanPham = sanPham;
        cardProductName.setText(sanPham.getTenSP());
        cardProductPrice.setText(String.valueOf(sanPham.getDonGia())+" đ");
        String path = "File:"+ sanPham.getAnhSP();
        image = new Image(path, 124, 124, false, true);
        cardProductImage.setImage(image);
    }

    public void addCTHD(MouseEvent mouseEvent) {
    }
}
