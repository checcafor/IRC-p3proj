import java.util.ArrayList;
import java.util.List;

public class Admin extends User {
    // private List<Admin> admin;
    private AdminActionStrategy action;

    public Admin(String username) {
        super(username);
        //admin = new ArrayList<>();
    }

    public AdminActionStrategy getAction() {
        return action;
    }

    public void setAction(AdminActionStrategy action) {
        this.action = action;
    }

    public void adminAction () {
        // contenente gli if

    }
}
