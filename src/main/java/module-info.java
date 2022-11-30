module de.fhswf.notenverwaltungws {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.fhswf.notenverwaltungws2223 to javafx.fxml;
    exports de.fhswf.notenverwaltungws2223;
}