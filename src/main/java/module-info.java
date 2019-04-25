module nl.overgaauw {
    requires javafx.controls;
    requires javafx.fxml;

    opens nl.overgaauw to javafx.fxml;
    exports nl.overgaauw;
}