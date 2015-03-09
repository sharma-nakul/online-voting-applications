package user;

import java.util.ArrayList;

import api.DisplayResult;

import com.fasterxml.jackson.annotation.JsonView;

public class Poll {

	@JsonView(DisplayResult.withoutResults.class)
	private String id;
	@JsonView(DisplayResult.withoutResults.class)
	private String question;
	@JsonView(DisplayResult.withoutResults.class)
	private String started_at;
	@JsonView(DisplayResult.withoutResults.class)
	private String expired_at;
	@JsonView(DisplayResult.withoutResults.class)
	private ArrayList<String> choice;
	@JsonView(DisplayResult.withResults.class)
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
		int i=0;
		while(i<choice.size())
		{
			results.add(new Integer(0));
			i+=1;
		}

	}

	public Poll() {
		choice = new ArrayList<String>();
		results = new ArrayList<Integer>();
	}

	public String getId() {
		return id;
	}

	public void setId() {
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
