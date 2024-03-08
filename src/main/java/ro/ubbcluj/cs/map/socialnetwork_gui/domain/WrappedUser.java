package ro.ubbcluj.cs.map.socialnetwork_gui.domain;
public class WrappedUser {

    private String firstName;
    private Long id;

    private String status;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String newStatus) {
        status = newStatus;
    }

    public WrappedUser(String firstName, Long id, String status) {
        this.firstName = firstName;
        this.id = id;
        this.status = status;
    }
}
