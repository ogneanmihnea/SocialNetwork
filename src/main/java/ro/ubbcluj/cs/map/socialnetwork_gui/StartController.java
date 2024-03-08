package ro.ubbcluj.cs.map.socialnetwork_gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubbcluj.cs.map.socialnetwork_gui.controllers.*;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.WrappedUser;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Page;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Pageable;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.MessageService;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.UtilizatorChangeEvent;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.observer.Observable;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.observer.Observer;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StartController implements Observer<UtilizatorChangeEvent> {

    Service<Long> serviceUser;
    MessageService<Long> serviceMessage;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    ObservableList<WrappedUser> modelWrappedUsers = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelUsersChat = FXCollections.observableArrayList();
    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableView<WrappedUser> tableViewFriendships;
    @FXML
    TableView<Utilizator> tableViewChat;

    @FXML
    TableColumn<WrappedUser, Long> tableColumnIDWrapped;
    @FXML
    TableColumn<WrappedUser, String> tableColumnNumeWrapped;
    @FXML
    TableColumn<WrappedUser, String> tableColumnStatusWrapped;

    @FXML
    TableColumn<Utilizator, Long> tableColumnID;
    @FXML
    TableColumn<Utilizator, String> tableColumnNume;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume;

    @FXML
    TableColumn<Utilizator, Long> tableColumnIDChat;
    @FXML
    TableColumn<Utilizator, String> tableColumnNumeChat;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenumeChat;

    @FXML
    private Button btnLogIn, btnSignUp, btnLogOut;
    @FXML
    private Button btnAdd, btnUpdate, btnFriendshipFromUser, btnUserFromFriendship;
    @FXML
    private Button btnChatFromUser, btnChatFromFriendship, btnUserFromChat, btnFriendshipFromChat;
    @FXML
    private Button btnSendRequest, btnDeclineFriendship, btnAcceptFriendship, btnDeleteFriend, btnNewMessage, btnMessages;

    @FXML
    private Label myUserNumeFromFriendship, myUserPrenumeFromFriendship;
    @FXML
    private Label myUserNumeFromChat, myUserPrenumeFromChat;

    @FXML
    private Button previousBtn, nextBtn;
    @FXML
    private TextField numberUsers, usernameField;
    @FXML
    private PasswordField passwordField;


    private Utilizator myUser;

    @FXML
    BorderPane userBorderPane, startApp;
    @FXML
    BorderPane friendshipBorderPane, chatBorderPane;

    public void setService(Service<Long> serviceUser, MessageService<Long> serviceMessage) {
        this.serviceUser = serviceUser;
        this.serviceMessage = serviceMessage;
        this.serviceUser.addObserver(this);
        initModelUsers();
    }

    private void initModelUsers() {
//        Iterable<Utilizator> allUsers = serviceUser.getAllUsers();
//        List<Utilizator> usersList = StreamSupport.stream(allUsers.spliterator(), false)
//                .toList();
//        model.setAll(usersList);

        Page<Utilizator> usersOnCurrentPage = serviceUser.getUsersOnPage(new Pageable(currentPage, numberOfRecordsPerPage));
        totalNumberOfElements = usersOnCurrentPage.getTotalNumberOfElements();
        List<Utilizator> userList = StreamSupport.stream(usersOnCurrentPage.getElementsOnPage().spliterator(), false).toList();
        model.setAll(userList);
        handlePageNavigationChecks();

    }

    private void initModelFriendships(Long id) {
        Iterable<WrappedUser> allWrappedUsers = serviceUser.getWrappedUsers(id);
        List<WrappedUser> usersList = StreamSupport.stream(allWrappedUsers.spliterator(), false)
                .toList();
        modelWrappedUsers.setAll(usersList);
    }

    private void initModelChat(Long id) {
        Iterable<Utilizator> allUsers = serviceUser.getPrUtilizatorului(id);
        List<Utilizator> usersList = StreamSupport.stream(allUsers.spliterator(), false)
                .toList();
        modelUsersChat.setAll(usersList);
    }

    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
    }

    public void initializeFriendships() {
        myUserNumeFromFriendship.setText(myUser.getFirstName());
        myUserPrenumeFromFriendship.setText(myUser.getLastName());
        tableColumnIDWrapped.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNumeWrapped.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnStatusWrapped.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableColumnStatusWrapped.setCellFactory(tv -> new TableCell<WrappedUser, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);


                // Verifică statusul și setează culoarea de fundal corespunzătoare
                if ("ACCEPTED".equals(status)) {
                    setStyle("-fx-background-color: #80fa80;");
                } else if ("REJECTED".equals(status)) {
                    setStyle("-fx-background-color: #e86177;");
                } else if ("PENDING".equals(status)) {
                    setStyle("-fx-background-color: #e5f367;");
                } else if ("NEW FRIEND REQUEST".equals(status)) {
                    setStyle("-fx-background-color: #E5F367;");
                }

                setText(status);
            }
        });

        tableViewFriendships.setItems(modelWrappedUsers);
    }

    public void initializeChat() {
        myUserNumeFromChat.setText(myUser.getFirstName());
        myUserPrenumeFromChat.setText(myUser.getLastName());
        tableColumnIDChat.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNumeChat.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnPrenumeChat.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableViewChat.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableViewChat.setItems(modelUsersChat);
    }

    private void setMyUser(Utilizator newUser) {
        myUser = newUser;
    }

    @FXML
    void handleButtonClick(ActionEvent event) throws IOException {
        if(event.getSource() == btnSignUp){
            signInViewFunction("views/signup-view.fxml");
        }
        if(event.getSource() == btnLogOut){
            setMyUser(null);
            startApp.setVisible(true);
            userBorderPane.setVisible(false);
        }
        if(event.getSource() == btnLogIn){
            String username=usernameField.getText();
            String password=passwordField.getText();
            usernameField.clear();
            passwordField.clear();
            Utilizator u = serviceUser.connect(username,password);

            if (u!=null)
            {
                startApp.setVisible(false);
                userBorderPane.setVisible(true);
                myUser = u;
            }
            else
            {
                MessageAlert.showErrorMessage(null, "Username sau parola gresita!");
            }
        }
        if (event.getSource() == btnAdd) {
            showUtilizatorFunction(null, "views/add-view.fxml");
        }
        if (event.getSource() == btnUpdate) {
            Utilizator utilizator = tableView.getSelectionModel().getSelectedItem();
            if (utilizator != null) {
                showUtilizatorFunction(utilizator, "views/update-view.fxml");
            }
        }
        if (event.getSource() == btnUserFromFriendship || event.getSource() == btnUserFromChat) {
            userBorderPane.setVisible(true);
            friendshipBorderPane.setVisible(false);
            chatBorderPane.setVisible(false);
            tableView.getSelectionModel().clearSelection();
            ///setMyUser(null);
        }
        if (event.getSource() == btnFriendshipFromUser || event.getSource() == btnFriendshipFromChat) {
            Utilizator utilizator = myUser;
//            if (myUser == null) {
//                utilizator = tableView.getSelectionModel().getSelectedItem();
//            } else {
//                utilizator = myUser;
//            }
            if (utilizator != null) {
                ///setMyUser(utilizator);
                userBorderPane.setVisible(false);
                friendshipBorderPane.setVisible(true);
                chatBorderPane.setVisible(false);
                initModelFriendships(utilizator.getId());
                initializeFriendships();
            } else {
                MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
            }
        }
        if (event.getSource() == btnChatFromUser || event.getSource() == btnChatFromFriendship) {
            Utilizator utilizator = myUser;
//            if (myUser == null) {
//                utilizator = tableView.getSelectionModel().getSelectedItem();
//            }
//            else {
//                utilizator = myUser;
//            }
            if (utilizator != null) {
                ///setMyUser(utilizator);
                userBorderPane.setVisible(false);
                friendshipBorderPane.setVisible(false);
                chatBorderPane.setVisible(true);
                initModelChat(utilizator.getId());
                initializeChat();
            } else {
                MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
            }
        }
        if (event.getSource() == btnSendRequest) {
            WrappedUser selectedUser = tableViewFriendships.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                if (selectedUser.getStatus().equals("PENDING")) {
                    MessageAlert.showErrorMessage(null, "Deja exista o cerere pentru acesta!");
                    return;
                }
                if (selectedUser.getStatus().equals("ACCEPTED")) {
                    MessageAlert.showErrorMessage(null, "Deja esti prieten cu aceasta persoana!");
                    return;
                }
                if (selectedUser.getStatus().equals("You have Request")) {
                    MessageAlert.showErrorMessage(null, "Ai o cerere deja de la aceasta persoana!");
                    return;
                }
                serviceUser.addFriendship(myUser.getId(), selectedUser.getId());
                initModelFriendships(myUser.getId());
                initializeFriendships();
                tableViewFriendships.getSelectionModel().clearSelection();
            } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un utilizator!");
        }
        if (event.getSource() == btnDeleteFriend) {
            WrappedUser selectedUser = tableViewFriendships.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                MessageAlert.showErrorMessage(null, "Nu ati selectat nici un utilizator!");
                return;
            }
            if (selectedUser.getStatus().equals("REJECTED")) {
                MessageAlert.showErrorMessage(null, "Nu esti prieten cu aceasta persoana!");
                return;
            }
            if (selectedUser.getStatus().equals("NEW FRIEND REQUEST")) {
                MessageAlert.showErrorMessage(null, "Nu poti sa stergi pentru ca nu esti inca prieten!");
                return;
            }
            if (selectedUser.getStatus().equals("PENDING")) {
                MessageAlert.showErrorMessage(null, "Inca nu ai primit raspuns!");
                return;
            }
            serviceUser.deleteFriendship(myUser.getId(), selectedUser.getId());
            initModelFriendships(myUser.getId());
            initializeFriendships();
            tableViewFriendships.getSelectionModel().clearSelection();
        }
        if (event.getSource() == btnAcceptFriendship) {
            WrappedUser selectedUser = tableViewFriendships.getSelectionModel().getSelectedItem();
            if (selectedUser == null)
                return;
            if (selectedUser.getStatus().equals("REJECTED"))
                return;
            if (selectedUser.getStatus().equals("PENDING"))
                return;
            if (selectedUser.getStatus().equals("ACCEPTED"))
                return;
            serviceUser.addFriendship(myUser.getId(), selectedUser.getId());
            initModelFriendships(myUser.getId());
            initializeFriendships();
            tableViewFriendships.getSelectionModel().clearSelection();
        }
        if (event.getSource() == btnDeclineFriendship) {
            WrappedUser selectedUser = tableViewFriendships.getSelectionModel().getSelectedItem();
            if (selectedUser == null)
                return;
            if (selectedUser.getStatus().equals("REJECTED"))
                return;
            if (selectedUser.getStatus().equals("ACCEPTED"))
                return;
            serviceUser.deleteFriendship(myUser.getId(), selectedUser.getId());
            initModelFriendships(myUser.getId());
            initializeFriendships();
            tableViewFriendships.getSelectionModel().clearSelection();
        }
        if(event.getSource() == btnNewMessage){
            List<Utilizator> myList = tableViewChat.getSelectionModel().getSelectedItems();
            if (!myList.isEmpty()) {
                newViewListFunction(myList, myUser,"views/newMessage-view.fxml");
            }
        }
        if(event.getSource() == btnMessages){
            Utilizator user = tableViewChat.getSelectionModel().getSelectedItem();
            if (user != null) {
                newViewListFunction(user, myUser,"views/allMessages-view.fxml");
            }
        }
    }

    public void showUtilizatorFunction(Utilizator utilizator, String name) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(name));//"add-view.fxml"


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AddController editMessageViewController = loader.getController();
            editMessageViewController.setService(serviceUser, dialogStage, utilizator);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signInViewFunction(String name) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(name));//"add-view.fxml"


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SignInController ViewController = loader.getController();
            ViewController.setService(serviceUser, dialogStage);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newViewListFunction(List<Utilizator> sendToList, Utilizator myUser, String name) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(name));//"add-view.fxml"


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Send Messsage");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            newMessageController newMessController = loader.getController();
            newMessController.setService(serviceMessage, dialogStage,myUser, sendToList);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newViewListFunction(Utilizator toUser, Utilizator myUser, String name) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(name));//"add-view.fxml"


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("All Messsages");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AllMessagesController newMessController = loader.getController();
            newMessController.setService(serviceMessage, dialogStage,myUser, toUser);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean r = serviceUser.deleteUser(selected.getId());
            if (r)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Utilizatorul a fost sters cu succes!");
        } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");
    }
    private int currentPage=0;
    private int numberOfRecordsPerPage = 5;
    private int totalNumberOfElements;
    private void handlePageNavigationChecks(){
        previousBtn.setDisable(currentPage == 0);
        nextBtn.setDisable((currentPage+1)*numberOfRecordsPerPage >= totalNumberOfElements);
    }
    public void goToNextPage(ActionEvent actionEvent) {
        currentPage++;
        initModelUsers();
    }

    public void goToPreviousPage(ActionEvent actionEvent) {
        currentPage--;
        initModelUsers();
    }
    public void handlePageNumber(ActionEvent actionEvent){
        String text=numberUsers.getText();
        if (isNumeric(text) && Integer.parseInt(text) > 0) {
            // Valid number
            currentPage=0;
            numberOfRecordsPerPage=Integer.parseInt(text);
            initModelUsers();
        }
    }
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+");
    }


    @Override
    public void update(UtilizatorChangeEvent utilizatorChangeEvent) {
        initModelUsers();
    }
}
