module com.kensoftph.mp3player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.kensoftph.mp3player to javafx.fxml;
    exports com.kensoftph.mp3player;
}