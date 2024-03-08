package ro.ubbcluj.cs.map.socialnetwork_gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.ValidationException;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;
    @FXML
    private TextField numeField;
    @FXML
    private TextField prenumeField;

    private Service<Long> service;
    Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setService(Service<Long> service, Stage stage) {
        this.service = service;
        this.dialogStage = stage;
    }

    @FXML
    public void handleSave() {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        Utilizator utilizator = new Utilizator(nume, prenume,username,password);
        utilizator.setId(1L);
        handleSave(utilizator);
    }

    public void handleSave(Utilizator u) {
        try {
            boolean r = this.service.addUser(u);
            if (r)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Salvare mesaj","Ai creat cu succes cont");
            dialogStage.close();
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
