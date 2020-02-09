package cn.mon.test;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.ExecutableRemoveOperation.ExecutableRemove;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import cn.mon.entity.Address;
import cn.mon.entity.Comment;
import cn.mon.entity.Favorites;
import cn.mon.entity.User;

/**
 * 使用Mongo tempelate 操作
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class test2 {
	
	@Resource
	private MongoOperations tempelate;
	
	@Test
	public void insertTest() {
		User user = new User();
		user.setName("徐凤年");
		user.setCountry("CN");
		user.setAge(23);
		user.setLenght(1.70f);
		user.setSalary(new BigDecimal("123.1234"));
		
		Address address = new Address();
		address.setaCode("41553");
		address.setAdd("北凉.凤");
		user.setAddress(address);
		Favorites favorites = new Favorites();
		favorites.setCites(Arrays.asList("杭州","北京"));
		favorites.setMovies(Arrays.asList("囧妈","北京欢迎您"));
		Comment comment1 = new Comment("太监", "我哪知道", new Date());
		Comment comment2 = new Comment("总管", "emmmmm", new Date());
		user.setComments(Arrays.asList(comment1,comment2));
		user.setFavorites(favorites);
		
		
		User user2 = new User();
		user2.setName("温不胜");
		user2.setCountry("CN");
		user2.setAge(23);
		user2.setLenght(1.70f);
		user2.setSalary(new BigDecimal("123.1234"));
		
		Address address2 = new Address();
		address2.setaCode("41553");
		address2.setAdd("北凉.胜");
		user2.setAddress(address2);
		Favorites favorites2 = new Favorites();
		favorites2.setCites(Arrays.asList("离阳","北京"));
		favorites2.setMovies(Arrays.asList("囧妈","北京欢迎您"));
		user2.setFavorites(favorites2);
		Comment comment3 = new Comment("太监", "我哪知道", new Date());
		Comment comment4 = new Comment("总管", "emmmmm", new Date());
		user2.setComments(Arrays.asList(comment3,comment4));
		Collection<User> insertAll = tempelate.insertAll(Arrays.asList(user,user2));
		for (User collects : insertAll) {
			//看一下写进数据库什么样的数据
			System.err.println(collects);
		}
	}
	
	@Test
	public void findTest(){
		List<User> findAll = tempelate.findAll(User.class);
		for (User user : findAll) {
			System.err.println(user);
		}
	}
	
	/**
	 * 更新
	 */
	@Test
	public void updateTest() {
		//1,先查出要更新的数据
		Query query = query(where("name").is("温不胜"));//来自于query包的静态导入
		Update update = new Update();//来自于update包的静态导入
		update.set("age",21);
		update.set("address.aCode", "123456");
		UpdateResult updateMulti = tempelate.updateMulti(query, update, User.class);
		System.out.println(updateMulti.getModifiedCount());
	}
	
	
	@Test
	public void deleteTest(){
		ExecutableRemove<User> remove = tempelate.remove(User.class);
		DeleteResult all = remove.all();
		System.out.println("--------------------->"+all.getDeletedCount());
	}
	
	
}
