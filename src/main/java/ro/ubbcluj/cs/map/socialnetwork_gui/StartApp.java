package ro.ubbcluj.cs.map.socialnetwork_gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Message;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Tuple;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.MessageValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.PrietenieValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.UtilizatorValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.*;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.PagingRepository;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.MessageService;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;

import java.io.IOException;
import java.util.Objects;

public class StartApp extends Application {

    String url="jdbc:postgresql://localhost:5432/socialnetwork";

    String username = "postgres";

    String password = "12345678";
    Repository<Long, Utilizator> userRepository;

    PagingRepository<Long, Utilizator> userPagingRepo;

    Repository<Tuple<Long,Long>, Prietenie> friendshipRepository;

    Repository<Long, Message> messageRepository;
    Service<Long> serviceUser;

    MessageService<Long> serviceMessage;

    public static void main(String[] args) {
        //System.out.println("ok");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        userRepository = new UserDBRepository(url, username, password, new UtilizatorValidator());

        friendshipRepository = new FriendshipDBRepository(url, username, password, new PrietenieValidator());

        messageRepository = new MessageDBRepository(url,username,password, new MessageValidator());

        userPagingRepo = new UserDBPagingRepository(url, username, password, new UtilizatorValidator());

        serviceUser = new Service<>(userRepository,friendshipRepository,userPagingRepo);
        serviceMessage = new MessageService<>( messageRepository);

        initView(primaryStage);

        primaryStage.setTitle("SocialNetworkApp");
        Image icon=new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/Social Network.png")));
        primaryStage.getIcons().add(icon);

        primaryStage.setWidth(839);
        primaryStage.setHeight(578);

        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader messageLoader = new FXMLLoader();
        messageLoader.setLocation(getClass().getResource("views/user-view.fxml"));
        AnchorPane messageTaskLayout = messageLoader.load();
        primaryStage.setScene(new Scene(messageTaskLayout));

        StartController startController = messageLoader.getController();
        startController.setService(serviceUser,serviceMessage);

    }
}
