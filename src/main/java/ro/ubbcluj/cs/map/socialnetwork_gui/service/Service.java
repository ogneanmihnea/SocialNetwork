package ro.ubbcluj.cs.map.socialnetwork_gui.service;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.*;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.ValidationException;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.Repository;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Page;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Pageable;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.PagingRepository;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.ChangeEventType;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.UtilizatorChangeEvent;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.codec.digest.DigestUtils;

public class Service<ID> implements ServiceInterface<ID> {

    ///private Long idNumber = 1000L;
    private Repository usersRepo;

    private Repository friendshipRepo;

    private PagingRepository<Long, Utilizator> usersPagingRepo;

    public Service(Repository repo, Repository friendshipRepo, PagingRepository usersPagingRepo) {
        this.friendshipRepo = friendshipRepo;
        this.usersRepo = repo;
        this.usersPagingRepo = usersPagingRepo;
    }

    private String encodePassword(String password) {
        // Hash cu SHA-256
        return DigestUtils.sha256Hex(password);
    }


    @Override
    public boolean addUser(Utilizator User) {
        ///idNumber++;
        ///User.setId(idNumber);
        String hashedPassword = encodePassword(User.getPassword());
        User.setPassword(hashedPassword);
        Optional<Utilizator> u = usersRepo.save(User);
        if (u.isEmpty()) {
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.ADD, null));
        }
        return u.isEmpty();
    }

    public boolean updateUser(Utilizator User) {
        ///idNumber++;
        ///User.setId(idNumber);
        Optional<Utilizator> u = usersRepo.update(User);
        if (u.isEmpty()) {
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.UPDATE, null));
        }
        return u.isEmpty();
    }

    @Override
    public boolean deleteUser(ID id) {
//        Utilizator user = getUserByID(id);
//        Iterable<Utilizator> vec = getPrUtilizatorului(id);
//        if (vec != null) {
//            vec.forEach(ut -> {
//                Tuple<ID, Long> myT = new Tuple<>(id, ut.getId());
//                Tuple<Long, ID> myTInv = new Tuple<>(ut.getId(), id);
//                friendshipRepo.delete(myT);
//                friendshipRepo.delete(myTInv);
//                ut.removeFriend(user);
//            });
//        }
        Optional<Utilizator> u = usersRepo.delete(id);
        if (u.isPresent()) {
            notifyObservers(new UtilizatorChangeEvent(ChangeEventType.DELETE, null));
        }
        return u.isPresent();
    }

    @Override
    public Iterable<Utilizator> getAllUsers() {
        return usersRepo.findAll();
    }

    public Utilizator getUserByID(ID id) {
        Iterable<Utilizator> all = getAllUsers();
        Iterable<Prietenie> allFr = getAllFriendships();
        if (!all.iterator().hasNext()) return null;
        for (var a : all) {
            if (a.getId().equals(id)) {
                for (var f : allFr) {
                    if (f.getId().getLeft().equals(id)) {
                        var utilizator = usersRepo.findOne(f.getId().getRight());
                        a.addFriend((Utilizator) utilizator.get());
                    } else if (f.getId().getRight().equals(id)) {
                        var utilizator = usersRepo.findOne(f.getId().getLeft());
                        a.addFriend((Utilizator) utilizator.get());
                    }
                }
                return a;
            }
        }
        return null;
    }

    @Override
    public boolean addFriendship(ID id1, ID id2) {
        Utilizator ut1 = getUserByID(id1);
        Utilizator ut2 = getUserByID(id2);
        if (ut1 == null || ut2 == null)
            throw new ValidationException("Unul dintre id-uri nu exista!");
        LocalDateTime date = LocalDateTime.now();
        Prietenie prietenie = new Prietenie(ut1, ut2, date);
        Prietenie prietenie2 = new Prietenie(ut2, ut1, date);
        var all = getAllFriendships();

        if (all.iterator().hasNext())
            for (Prietenie p : all) {
                if (p.getId().equals(prietenie.getId()))// p.getId().equals(prietenie2.getId())
                    return false;
                if (p.getId().equals(prietenie2.getId())) {
                    if (p.getAcceptance().equals(FriendRequestEnum.PENDING)) {
                        prietenie2.setAcceptance(FriendRequestEnum.ACCEPTED);
                        friendshipRepo.update(prietenie2);
                    }
                    return false;
                }
            }
        prietenie.setAcceptance(FriendRequestEnum.PENDING);
        if (friendshipRepo.save(prietenie).isEmpty()) {
            ut1.addFriend(ut2);
            ut2.addFriend(ut1);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFriendship(ID id1, ID id2) {
//        Utilizator user1 = getUserByID(id1);
//        Utilizator user2 = getUserByID(id2);
//        Tuple<ID, ID> myT2 = new Tuple<>(id2, id1);
//        if (friendshipRepo.delete(myT).isPresent() || friendshipRepo.delete(myT2).isPresent()) {
//            user1.removeFriend(user2);
//            user2.removeFriend(user1);
//            return true;
//        }
//        return false;
        Tuple<ID, ID> myT = new Tuple<>(id1, id2);
        return friendshipRepo.delete(myT).isPresent();
    }


    @Override
    public Iterable<Prietenie> getAllFriendships() {
        return friendshipRepo.findAll();
    }

    public Iterable<Utilizator> getPrUtilizatorului(ID id) {
        return Optional.ofNullable(getUserByID(id))
                .map(Utilizator::getFriends)
                .orElse(null);
    }

    public List<Utilizator> DFS(Utilizator u, Set<Utilizator> set) {
        List<Utilizator> finallist = new ArrayList<>();
        finallist.add(u);
        set.add(u);
        Utilizator u1 = getUserByID((ID) u.getId());
        u1.getFriends().forEach(f -> {
            if (!set.contains(f)) {
                List<Utilizator> l = DFS(f, set);
                finallist.addAll(l);
            }
        });
        return finallist;
    }

    public int Comunitati() {
        Iterable<Utilizator> all = getAllUsers();
        Set<Utilizator> set = new HashSet<>();
        AtomicInteger nr = new AtomicInteger();
        if (all != null) {
            all.forEach(u -> {
                if (!set.contains(u)) {
                    nr.getAndIncrement();
                    DFS(u, set);
                }
            });
        }
        return nr.get();
    }

    public List<Utilizator> Sociabila() {
        AtomicReference<List<Utilizator>> most = new AtomicReference<>(new ArrayList<>());
        Iterable<Utilizator> all = getAllUsers();
        Set<Utilizator> set = new HashSet<>();
        AtomicInteger max = new AtomicInteger(-1);
        if (all != null) {
            all.forEach(u -> {
                if (!set.contains(u)) {
                    List<Utilizator> list = DFS(u, set);
                    if (list.size() > max.get()) {
                        most.set(list);
                        max.set(list.size());
                    }
                }
            });
        }

        return most.get();
    }

    public List<Prietenie> ShowPrLuna(ID id, String month) {
        Iterable<Prietenie> all = getAllFriendships();
        if (all == null) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(all.spliterator(), false)
                .filter(p -> (p.getUser1().getId().equals(id) || p.getUser2().getId().equals(id)) &&
                        p.getDate().getMonth().toString().equals(month))
                .collect(Collectors.toList());
    }

    public Iterable<WrappedUser> getWrappedUsers(Long id) {
        Iterable<Utilizator> allUsers = getAllUsers();
        Set<WrappedUser> wrappedFriendships = new HashSet<>();
        for (var a : allUsers) {
            if (!a.getId().equals(id)) {
                Optional<Prietenie> pr = friendshipRepo.findOne(new Tuple<>(a.getId(), id));
                if (pr.isPresent()) {
                    if (pr.get().getAcceptance().equals(FriendRequestEnum.ACCEPTED)) {
                        WrappedUser u = new WrappedUser(a.getFirstName(), a.getId(), "ACCEPTED");
                        wrappedFriendships.add(u);
                    } else {
                        if (pr.get().getId().getRight().equals(id)) {
                            WrappedUser u = new WrappedUser(a.getFirstName(), a.getId(), "NEW FRIEND REQUEST");
                            wrappedFriendships.add(u);
                        } else {
                            WrappedUser u = new WrappedUser(a.getFirstName(), a.getId(), pr.get().getAcceptance().toString());
                            wrappedFriendships.add(u);
                        }
                    }
                } else {
                    WrappedUser u = new WrappedUser(a.getFirstName(), a.getId(), "REJECTED");
                    wrappedFriendships.add(u);
                }
            }
        }
        return wrappedFriendships;
    }

    private List<Observer<UtilizatorChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<UtilizatorChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorChangeEvent t) {
        observers.forEach(o -> o.update(t)); ///.stream()
    }

    public Page<Utilizator> getUsersOnPage(Pageable pageable){
        return usersPagingRepo.findAll(pageable);
    }


    public Utilizator connect(String username,String password){

        Iterable<Utilizator> all=usersRepo.findAll();
        password=encodePassword(password);

        for (Utilizator u:all){
            if (Objects.equals(u.getUsername(), username) && Objects.equals(u.getPassword(), password))
            {
                return u;
            }
        }
        return null;
    }


}