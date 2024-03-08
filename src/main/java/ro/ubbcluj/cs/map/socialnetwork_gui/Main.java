package ro.ubbcluj.cs.map.socialnetwork_gui;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Message;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Tuple;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.MessageValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.PrietenieValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.UtilizatorValidator;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.MessageDBRepository;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.Repository;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.UserDBRepository;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.PagingRepository;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.MessageService;
import ro.ubbcluj.cs.map.socialnetwork_gui.service.Service;
import ro.ubbcluj.cs.map.socialnetwork_gui.ui.Console;

public class Main {

    public static void main(String[] args) {


        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "12345678";

        ///InMemoryRepository<Long, Utilizator> userRepository=new InMemoryRepository<>(new UtilizatorValidator());
        ///InMemoryRepository<Tuple<Long,Long>, Prietenie> friendshipRepository=new InMemoryRepository<>(new PrietenieValidator());
        ///Utilizator u1=new Utilizator("uFirstName", "LastName");u1.setId(10L);
        ///Utilizator u2=new Utilizator("umami", "LastName"); u2.setId(11L);

        ///userRepository.save(u1);
        ///userRepository.save(u2);
        Repository<Long, Utilizator> userRepository = new ro.ubbcluj.cs.map.socialnetwork_gui.repository.UserDBRepository(url, username, password, new UtilizatorValidator());
        PagingRepository<Long, Utilizator> userPagingRepository = new ro.ubbcluj.cs.map.socialnetwork_gui.repository.UserDBPagingRepository(url, username, password, new UtilizatorValidator());
        Repository<Tuple<Long,Long>, Prietenie> friendshipRepository = new ro.ubbcluj.cs.map.socialnetwork_gui.repository.FriendshipDBRepository(url, username, password, new PrietenieValidator());
        Repository<Long, Message> messageRepository = new ro.ubbcluj.cs.map.socialnetwork_gui.repository.MessageDBRepository(url, username, password, new MessageValidator());

        Service<Long> serv = new Service<>(userRepository,friendshipRepository,userPagingRepository);
        MessageService<Long> servMess = new MessageService<>( messageRepository);
        Console cons = new Console(serv);
        cons.execute();

        //userRepository.findAll().forEach(System.out::println);
        ///System.out.println(userRepository.findOne(1L));




    }
}
