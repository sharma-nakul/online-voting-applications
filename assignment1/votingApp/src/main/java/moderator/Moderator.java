package moderator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import poll.Poll;

public class Moderator {

	private int id;
	private String name;
	private String email;
	private String password;
	private String created_at;
	private static int nextId = 12345; // to generate the random id
	private ArrayList<Poll> pollList;

	public Moderator(String name, String email, String password) {
		this.id =generateId(); // return generated id
		this.name = name;
		this.email = email;
		this.password = password;
		this.setCreated_at(); // set current date
		pollList = new ArrayList<Poll>(); // create memory space for new
											// moderator poll
	}

	// Method returns Moderator's Poll by passing poll id as a parameter
	public Poll getModeratorPoll(String polId) {
		Poll p = null;

		// Iterate through poll list to get poll of provided polId
		Iterator<Poll> it = pollList.iterator();
		while (it.hasNext()) {
			if (it.next().getId().equals(polId)) {
				p = it.next();
				break;
			}
		}
		return p;
	}

	public void deleteModeratorPoll(String polId) {
		// Return poll from moderator's poll list and then remove it.
		this.pollList.remove(getModeratorPoll(polId));

	}

	public final Integer generateId() {
		nextId += nextId;
		return Integer.valueOf(nextId);
	}

	public Moderator() {
	} // Default constructor

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

	public void setCreated_at() {
		LocalDateTime ldt = LocalDateTime.now();
		this.created_at = ldt.toString() + "Z";
	}

	/*
	 * public ArrayList<Poll> getPollList() { return pollList; }
	 */
	public void setPollList(ArrayList<Poll> pollList) {
		this.pollList = pollList;
	}

}
