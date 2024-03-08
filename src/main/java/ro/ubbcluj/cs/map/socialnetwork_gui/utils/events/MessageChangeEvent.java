package ro.ubbcluj.cs.map.socialnetwork_gui.utils.events;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Message;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.utils.events.Event;

public class MessageChangeEvent implements Event {
    private ChangeEventType type;
    private Message data, oldData;

    public MessageChangeEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }
    public MessageChangeEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}