package user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Moderator {

    private int id;

    private String name;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;
    private String created_at;
    private int nextId; // to generate the random id
    private ArrayList<Poll> pollList;

    public Moderator(String name, String email, String password) {
        this.id = nextId; // return generated id
        this.name = name;
        this.email = email;
        this.password = password;
        this.setCreated_at(); // set current date
        pollList = new ArrayList<>(); // create memory space for new
        // moderator poll
    }

    public Moderator() {
    } // Default constructor

    // Method returns Moderator's Poll by passing poll id as a parameter
    public Poll getModeratorPoll(String polId) {
        Poll pol = null;
        for (Poll p : this.pollList) {
            if (p.getId().equals(polId)) {
                pol = p;
                break;
            }
        }
        return pol;
    }

    @JsonIgnore
    public ArrayList<String> getAllPollId() {
        Poll pol = null;
        ArrayList<String> allPollId = new ArrayList<>();
        for (Poll p : this.pollList) {
            //System.out.println(p.getId());
            allPollId.add(p.getId());
        }
        return allPollId;
    }

    public void deleteModeratorPoll(String polId) {
        // Return poll from moderator's poll list and then remove it.
        this.pollList.remove(getModeratorPoll(polId));

    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void putPollInList(Poll p) {
        this.pollList.add(p);
    }

    public void setCreated_at() {
        LocalDateTime ldt = LocalDateTime.now();
        this.created_at = ldt.toString() + "Z";
    }

    @JsonIgnore
    public ArrayList<Poll> getPollList() {
        return pollList;
    }

    public void setPollList(ArrayList<Poll> pollList)
    {
        this.pollList = pollList;
    }


}
