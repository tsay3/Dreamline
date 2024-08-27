module com.example.projectdreamline {
    requires javafx.controls;
    requires javafx.fxml;
    requires amadeus.java;
    requires javafx.web;
    requires java.sql;


    opens com.example.projectdreamline to javafx.fxml;
    exports com.example.projectdreamline;
}