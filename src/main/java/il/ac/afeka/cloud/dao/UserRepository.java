package il.ac.afeka.cloud.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserDao {
	private List<String> emails;
	
	public UserRepository() {
	}

	@PostConstruct
	void init() {
		this.emails = new ArrayList<String>();
		this.emails.add("shopper32@amazon.com");
		this.emails.add("user38@gmail.com");
	}

	@Override
	public boolean existsById(String email) {
		return this.emails.contains(email);
	}

}
