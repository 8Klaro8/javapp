module com.my {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.my to javafx.fxml;
    exports com.my;
}
