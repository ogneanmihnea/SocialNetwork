module ro.ubbcluj.cs.map.socialnetwork_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.commons.codec;

    opens ro.ubbcluj.cs.map.socialnetwork_gui to javafx.fxml;
    exports ro.ubbcluj.cs.map.socialnetwork_gui;
    opens ro.ubbcluj.cs.map.socialnetwork_gui.domain to javafx.base;
    opens ro.ubbcluj.cs.map.socialnetwork_gui.controllers to javafx.fxml;
}