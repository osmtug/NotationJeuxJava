module projet_jvm.projet_jvm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;

    opens projet_jvm to javafx.fxml;
    exports projet_jvm;
    opens projet_jvm.controller to javafx.fxml;
    exports projet_jvm.controller;
    opens projet_jvm.ui to javafx.fxml;
    opens projet_jvm.models to javafx.base;

    opens projet_jvm.models.users to javafx.base, javafx.fxml;
}