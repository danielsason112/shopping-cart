package il.ac.afeka.cloud.logic;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import il.ac.afeka.cloud.layout.usersManagement.User;

@Component
public class UsersManagementClient {
	private RestTemplate restTemplate;
	private String uri;
	
	public UsersManagementClient() {
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
	}
	
	@Value("${user-management-service.uri}")
	public void setUri(String uri) {
		this.uri = uri;
	}

	public User getUserByEmail(String email) {
		return this.restTemplate.getForObject(this.uri + "/users/{email}", User.class, email);
	}

}
