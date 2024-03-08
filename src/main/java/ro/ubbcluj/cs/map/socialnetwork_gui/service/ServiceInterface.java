package ro.ubbcluj.cs.map.socialnetwork_gui.service;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.UtilizatorChangeEvent;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.observer.Observable;

public interface ServiceInterface< ID > extends Observable<UtilizatorChangeEvent> {
    boolean addUser(Utilizator User);
    boolean deleteUser(ID id);

    Iterable<Utilizator> getAllUsers();

    boolean addFriendship(ID id1, ID id2);
    boolean deleteFriendship(ID id1, ID id2);

    Iterable<Prietenie> getAllFriendships();
}
