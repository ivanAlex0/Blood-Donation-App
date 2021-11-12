module DBproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.sql;
    requires org.apache.commons.pool2;
    requires commons.dbcp;

    opens sample to javafx.fxml;
    exports sample;
    exports user;
    exports database;
}
