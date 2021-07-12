package de.hse.swa.jodel.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.hse.swa.jodel.orm.dao.UserDao;
import de.hse.swa.jodel.orm.model.Post;
import de.hse.swa.jodel.orm.model.User;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class UserDaoTest {
	
	@Inject
	UserDao userDao;
	
	private User createUser(String prefix) {
		User user = new User();
		user.setUsername(prefix+"UserName");
		user.setPassword(prefix+"password");
		user.setGoogleId(prefix+1234);
		return user;
	}
	
	public void addTwoUsers() {
		User first = createUser("First");
		userDao.save(first);
		User second = createUser("Second");
		userDao.save(second);
	}
	
	private void printUser(List<User> users) {
		for(User user : users) {
            System.out.println(user.getUser_id() + "|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getGoogleId());
        }
	}
	
	@BeforeEach
	public void clearAllFromDatabase() {
		userDao.deleteAllOrmUsers();
	}
	
	
	@Test
	void addUser_2() {
		addTwoUsers();
		List<User> users = userDao.getUsers();
		assertEquals(users.size(),4);
		printUser(users);
	}
	
	 @Test
	 void checkLogin_1() {
	 	User first = createUser("third");
	 	userDao.save(first);
	 	List<User> persons = userDao.getUsers();
	 	assertNotNull(userDao.login(persons.get(0).getUsername(), persons.get(0).getPassword()));
	 }
	
}