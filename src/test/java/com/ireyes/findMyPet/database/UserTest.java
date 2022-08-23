package com.ireyes.findMyPet.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ireyes.findMyPet.dao.UserRepository;
import com.ireyes.findMyPet.dao.post.PostRepository;
import com.ireyes.findMyPet.model.pet.Pet;
import com.ireyes.findMyPet.model.post.Found;
import com.ireyes.findMyPet.model.post.Post;
import com.ireyes.findMyPet.model.post.Search;
import com.ireyes.findMyPet.model.user.User;

@DataJpaTest
public class UserTest {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PostRepository<Post> postRepo;
	
	@Test
	public void findUserWithOldPosts() {
		Calendar calendar = Calendar.getInstance();
		User carl = new User();
		User rick = new User();
		User glen = new User();
		
		Search dogSearch = new Search();
		dogSearch.setDate(calendar.getTime());
		dogSearch.setUser(carl);
		dogSearch.setPet(new Pet());
		Search doraSearch = new Search();
		doraSearch.setDate(calendar.getTime());
		doraSearch.setUser(glen);
		doraSearch.setPet(new Pet());
		Found dogFound = new Found();
		calendar.add(Calendar.MONTH, -2);
		dogFound.setDate(calendar.getTime());
		dogFound.setUser(glen);
		dogFound.setPet(new Pet());
		Found doraFound = new Found();
		calendar.add(Calendar.YEAR, -1);
		doraFound.setDate(calendar.getTime());
		doraFound.setUser(rick);
		doraFound.setPet(new Pet());
		
		postRepo.save(dogSearch);
		postRepo.save(doraSearch);
		postRepo.save(dogFound);
		postRepo.saveAndFlush(doraFound);
		
		List<User> users = userRepo.findWithPostBefore(Calendar.getInstance().getTime());
		
		assertThat(users).containsExactly(glen, rick);
		assertThat(users.size()).isEqualTo(2);
	}
}
