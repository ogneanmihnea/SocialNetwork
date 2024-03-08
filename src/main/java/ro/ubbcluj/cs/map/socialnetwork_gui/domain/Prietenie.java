package ro.ubbcluj.cs.map.socialnetwork_gui.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Prietenie extends Entity<Tuple<Long, Long>> {

    private Utilizator u1;
    private Utilizator u2;
    LocalDateTime date;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

    private FriendRequestEnum acceptance;

    public Prietenie(Utilizator u1, Utilizator u2, LocalDateTime newDate) {
        this.u1 = u1;
        this.u2 = u2;
        Tuple<Long, Long> myTuple = new Tuple<>(u1.getId(), u2.getId());
        this.setId(myTuple);
        date = newDate;
        this.acceptance = FriendRequestEnum.PENDING;
    }

    public Prietenie(Utilizator u1, Utilizator u2, FriendRequestEnum acceptance) {
        this.u1 = u1;
        this.u2 = u2;
        Tuple<Long, Long> myTuple = new Tuple<>(u1.getId(), u2.getId());
        this.setId(myTuple);
        date = LocalDateTime.now();
        this.acceptance = acceptance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Utilizator getUser1() {
        return u1;
    }

    public void setUser1(Utilizator user1) {
        this.u1 = user1;
    }

    public Utilizator getUser2() {
        return u2;
    }

    public void setUser2(Utilizator user2) {
        this.u2 = user2;
    }

    public FriendRequestEnum getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(FriendRequestEnum acceptance) {
        this.acceptance = acceptance;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "date=" + date.format(formatter) +
                ", id=" + id +
                ", request=" + acceptance +
                '}';
    }
}
