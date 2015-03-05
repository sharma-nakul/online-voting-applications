package user;

import org.hibernate.validator.constraints.NotEmpty;

public class Moderator {

	private int id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String created_at;

	/*-------------- Default Constructor-------------------*/
	public Moderator() {}

	/*-------------- Constructor Override------------------*/
	public Moderator(int id, String name, String email, String password,String created_at) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.created_at=created_at;
	}
	public Moderator(String email, String password)
	{
		this.email=email;
		this.password=password;
	}

	/*------------- Defining Getter and Setter ------------*/
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
}
