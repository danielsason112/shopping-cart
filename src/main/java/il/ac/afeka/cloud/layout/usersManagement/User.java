package il.ac.afeka.cloud.layout.usersManagement;

public class User {
	private String Email;

	public User() {
		super();
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	@Override
	public String toString() {
		return "User [Email=" + Email + "]";
	}
	
	
}
