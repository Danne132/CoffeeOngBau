module project.an.CoffeeOngBau {
    requires javafx.controls;
    requires javafx.fxml;

    opens project.an.CoffeeOngBau to javafx.fxml;
    exports project.an.CoffeeOngBau;
}
