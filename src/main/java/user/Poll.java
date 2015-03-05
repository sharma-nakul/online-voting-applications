package user;

import java.util.ArrayList;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.internal.NotNull;

public class Poll {
	
	private String id;
	
	@NotNull
	@NotEmpty
	private String question;
	
	@NotNull
	@NotEmpty
	private String started_at;
	
	@NotNull
	@NotEmpty
	private String expired_at;
	
	@NotNull
	@NotEmpty
	private ArrayList<String> choice=new ArrayList<String>();
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
	ArrayList<Integer> results=new ArrayList<Integer>();

	public Poll (){}
	public Poll (String id, String question, String started_at, String expired_at, ArrayList<String> choice )
	{
		this.id=id;
		this.question=question;
		this.started_at=started_at;
		this.expired_at=expired_at;
		this.choice=choice;
	}

	public Poll (String id, String question, String started_at, String expired_at, ArrayList<String> choice , ArrayList<Integer> results)
	{
		
		this.id=id;
		this.question=question;
		this.started_at=started_at;
		this.expired_at=expired_at;
		this.choice=choice;
		this.results=results;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	}

	public ArrayList<Integer> getResults() {
		return results;
	}

	public void setResults(ArrayList<Integer> results) {
		this.results = results;
	}
	
	
}
