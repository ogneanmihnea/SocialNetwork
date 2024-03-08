package ro.ubbcluj.cs.map.socialnetwork_gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Message;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.ValidationException;
import ro.ubbcluj.cs.map.socialnetwork_gui.StartController;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.MessageService;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class newMessageController {

    @FXML
    private TextField usersField;
    @FXML
    private Button btnSendMessage;
    private MessageService<Long> service;

    @FXML
    private TextArea messageArea;
    Stage dialogStage;
    List<Utilizator> sendToList;

    Utilizator myUser;

    @FXML
    private void initialize() {
        messageArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Handle the Enter key press
                handleSend();
                // Consume the event to prevent a new line in the TextArea
                event.consume();
            }
        });
        messageArea.setWrapText(true);
    }

    public void setService(MessageService<Long> service, Stage stage, Utilizator myUser, List<Utilizator> sendToList) {
        this.service = service;
        this.dialogStage = stage;
        this.sendToList = sendToList;
        this.myUser = myUser;
        if (!this.sendToList.isEmpty()) {
            setFields(sendToList);
            usersField.setEditable(false);
        }
    }



    @FXML
    public void handleSend() {
        String msg = messageArea.getText();
        Message myMessage = new Message(msg,myUser,sendToList,LocalDateTime.now());
        handleSend(myMessage);
    }

    public void handleSend(Message msg) {
        try {
            boolean r = this.service.addMessage(msg);
            if (r)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Adaugare mesaj", "Mesajul a fost adaugat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void setFields(List<Utilizator> myList) {
        String concatenatedNames = myList.stream()
                .map(Utilizator::getFirstName)
                .collect(Collectors.joining(", "));
        usersField.setText(concatenatedNames);
    }
}
