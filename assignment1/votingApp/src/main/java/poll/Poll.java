package poll;

import java.util.ArrayList;

public class Poll {

	private String id;
	private String question;
	private String started_at;
	private String expired_at;
	private ArrayList<String> choice;
	private ArrayList<Integer> results;
	private static int nextId = 54321;

	public Poll(String question, String started_at, String expired_at,
			ArrayList<String> choice) {
		this.setId();
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = new ArrayList<String>(choice);

		// Initialize results for all choices with zero
		results = new ArrayList<Integer>();
		for (int i = 0; i < choice.size(); i++)
			results.add(new Integer(0));

	}

	public Poll() {
		choice = new ArrayList<String>();
		results = new ArrayList<Integer>();
	}

	public String getId() {
		return id;
	}

	public void setId() {
		if (nextId != 54321)
			nextId += 10;
		this.id = Integer.toUnsignedString(nextId, 36).toUpperCase();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getStarted_at() {
		return started_at;
	}

	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}

	public String getExpired_at() {
		return expired_at;
	}

	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}

	public ArrayList<String> getChoice() {
		return choice;
	}

	public void setChoice(ArrayList<String> choice) {
		this.choice = choice;
		for (int i = 0; i < choice.size(); i++)
			results.add(new Integer(0));
	}

	public ArrayList<Integer> getResults() {
		return results;
	}

	public void setResults(ArrayList<Integer> results) {
		this.results = results;
	}

}
