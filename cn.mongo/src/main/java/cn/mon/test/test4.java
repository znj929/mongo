package cn.mon.test;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ExecutableRemoveOperation.ExecutableRemove;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.client.result.DeleteResult;

import cn.mon.entity.Address;
import cn.mon.entity.Comment;
import cn.mon.entity.Favorites;
import cn.mon.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class test4 {
	
	@Resource
	private MongoOperations tempelate;
	
	@Test
	public void deleteTest(){
		ExecutableRemove<User> remove = tempelate.remove(User.class);
		DeleteResult all = remove.all();
		System.out.println("--------------------->"+all.getDeletedCount());
	}
	
	/**
	 * 初始化加载数据
	 * @throws ParseException 
	 */
	@Test
	public void initInsert() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date commentDate = formatter.parse("2029-02-07 13:33:07");
		
		User user = new User();
		user.setName("a徐凤年");
		Favorites favorites = new Favorites();
		favorites.setCites(Arrays.asList("杭州","北京"));
		favorites.setMovies(Arrays.asList("囧妈","北京欢迎您","功夫"));
		Comment comment1 = new Comment("太监1", "我哪知道111", commentDate);
		Comment comment2 = new Comment("总管1", "emmmmm111", commentDate);
		Comment comment3 = new Comment("天下第一", "就是很好看", commentDate);
		Comment comment4 = new Comment("风流倜傥", "牛逼", commentDate);
		user.setComments(Arrays.asList(comment1,comment2,comment3,comment4));
		user.setFavorites(favorites);
		
		User user2 = new User();
		user2.setName("b温不胜");
		user2.setSalary(new BigDecimal("123.1234"));
		Favorites favorites2 = new Favorites();
		favorites2.setCites(Arrays.asList("离阳","北莽"));
		favorites2.setMovies(Arrays.asList("囧妈","疯狂的石头","华山论剑"));
		user2.setFavorites(favorites2);
		Comment comment5 = new Comment("太监555", "我哪知道111", commentDate);
		Comment comment6 = new Comment("总管1", "emmmmm111", commentDate);
		Comment comment7 = new Comment("天下第一77", "就是很好看", commentDate);
		Comment comment8 = new Comment("风流倜傥", "牛逼了", commentDate);
		Comment comment9 = new Comment("太监1", "我哪知道111", commentDate);
		user2.setComments(Arrays.asList(comment5,comment6,comment7,comment8,comment9));
		
		
		User user3 = new User();
		user3.setName("c曹官子");
		user3.setSalary(new BigDecimal("123.21"));
		Address address3 = new Address();
		address3.setaCode("41333");
		address3.setAdd("北凉.胜3");
		user3.setAddress(address3);
		Favorites favorites3 = new Favorites();
		favorites3.setCites(Arrays.asList("离阳","北莽"));
		favorites3.setMovies(Arrays.asList("囧妈","疯狂的石头"));
		user3.setFavorites(favorites3);
		Comment comment10 = new Comment("太监3", "我哪知道333", commentDate);
		Comment comment11 = new Comment("总管3", "emmmmm333", commentDate);
		user3.setComments(Arrays.asList(comment10,comment11));
		
		User user4 = new User();
		user4.setName("d洛阳");
		user4.setSalary(new BigDecimal("155.21"));
		Collection<User> insertAll = tempelate.insertAll(Arrays.asList(user,user2,user3,user4));
		for (User collects : insertAll) {
			//看一下写进数据库什么样的数据
			System.err.println(collects);
		}
	}
	
	/**
	 * sort，limit，skip
	 */
	@Test
	public void findTest(){
		Query query = new Query();
		//根据名字排序
		//query.with(Sort.by(Direction.DESC, "name"));
		//分页，
		query.skip(2).limit(2);
		List<User> find = tempelate.find(query , User.class);
		for (User user : find) {
			System.err.println(user);
		}
		long count = tempelate.count(query,  User.class);
		System.err.println(count);
	}
	
	/** ========================== 数组操作 ======================  */
	@Test
	public void findArray1(){
		//查询comments.author对象数组中包含(总管1)的数据
		//Query query = query(where("comments.author").is("总管1"));
		//查询favorites.movies数组等于("囧妈","疯狂的石头")的数据，严格按照数量排序来匹配
		//Query query = query(where("favorites.movies").is(Arrays.asList("囧妈","疯狂的石头")));
		//查询favorites.movies数组包含("疯狂的石头","囧妈")的数据，跟数量顺序无关
		//Query query = query(where("favorites.movies").all(Arrays.asList("疯狂的石头","囧妈")));
		//查询favorites.movies数组匹配到("囧妈","功夫")中任意一个的数据,
		//Query query = query(where("favorites.movies").in(Arrays.asList("囧妈","功夫")));
		//查询favorites.movies数组第一个为（囧妈）的数据
		Query query = query(where("favorites.movies.0").is("囧妈"));
		List<User> find = tempelate.find(query, User.class);
		for (User user : find) {
			System.err.println(user);
		}
	}
	/**
	 * 通过$slice返回数组中的部分数据
	 */
	@Test
	public void testArray2() {
		Query query = new Query();
		// 指定查询favorites对象中favorites.movies数组，排除favorites.movies数组第一个数据，取两个
		query.fields().include("favorites").slice("favorites.movies", 1, 2);
		List<User> find = tempelate.find(query, User.class);
		for (User user : find) {
			System.err.println(user.getFavorites());
		}
	}
	
	/**====================== 对象数组操作 ===================== **/
	/**
	 * 备注：对象数组精确查找
	 * @throws ParseException
	 */
	@Test
	public void testObjectArray1() throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date commentDate = formatter.parse("2029-02-07 13:33:07");
		Comment comment = new Comment();
		comment.setAuthor("太监3");
		comment.setCommentTime(commentDate);
		comment.setContent("我哪知道333");
		Query query = query(where("comments").is(comment));
		List<User> find = tempelate.find(query, User.class);
		for (User user : find) {
			System.err.println(user);
		}
		
	}
	
}
