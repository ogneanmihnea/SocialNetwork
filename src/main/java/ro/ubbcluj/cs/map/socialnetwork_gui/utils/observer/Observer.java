package ro.ubbcluj.cs.map.socialnetwork_gui.utils.observer;


import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}