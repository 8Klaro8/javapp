module com.my {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires org.json;
    requires java.desktop;


    opens com.my to javafx.fxml;
    exports com.my;
}
