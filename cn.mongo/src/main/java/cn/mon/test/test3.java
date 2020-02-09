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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mon.entity.Address;
import cn.mon.entity.Comment;
import cn.mon.entity.Favorites;
import cn.mon.entity.User;

/**
 * 查询语法
 * @author Administrator
 *
 * 运算符：
 * 	范围： $eq ==> 等于
 * 		$lt	==>	小于
 * 		$gt	==>	大于
 * 		$lte==>	小于等于
 * 		$gte==>	大于等于
 * 		$in	==>	判断元素是否在指定的集合范围内
 * 		$all==>	判断数组是否包含某几个元素，无关顺序
 * 		$nin==>	判断元素是否不在指定的集合范围里
 * 	布尔运算：$ne	==>	不等于，不匹配参数条件
 * 		   $not	==>	不匹配结果
 * 		   $or	==>	有一个条件成立则匹配
 * 		   $nor	==>	所有条件都不匹配
 * 		   $and	==>	所有条件都必须匹配
 * 		   $exists==>判断元素是否存在
 * 	其他：$regex	==>	正则表达式匹配
 * 		.		==>	子文档匹配
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class test3 {
	
	@Resource
	private MongoOperations tempelate;
	
	/**
	 * 初始化加载数据
	 */
	@Test
	public void initInsert() {
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
		favorites.setMovies(Arrays.asList("囧妈","北京欢迎您","功夫"));
		Comment comment1 = new Comment("太监1", "我哪知道111", new Date());
		Comment comment2 = new Comment("总管1", "emmmmm111", new Date());
		user.setComments(Arrays.asList(comment1,comment2));
		user.setFavorites(favorites);
		
		User user2 = new User();
		user2.setName("温不胜");
		user2.setCountry("CN");
		user2.setAge(23);
		user2.setLenght(1.80f);
		user2.setSalary(new BigDecimal("123.1234"));
		Address address2 = new Address();
		address2.setaCode("41222");
		address2.setAdd("北凉.胜");
		user2.setAddress(address2);
		Favorites favorites2 = new Favorites();
		favorites2.setCites(Arrays.asList("离阳","北莽"));
		favorites2.setMovies(Arrays.asList("囧妈","疯狂的石头","华山论剑"));
		user2.setFavorites(favorites2);
		Comment comment3 = new Comment("太监2", "我哪知道222", new Date());
		Comment comment4 = new Comment("总管2", "emmmmm222", new Date());
		user2.setComments(Arrays.asList(comment3,comment4));
		
		
		User user3 = new User();
		user3.setName("曹官子");
		user3.setCountry("CN");
		user3.setAge(56);
		//user3.setLenght(1.80f);
		user3.setSalary(new BigDecimal("123.21"));
		Address address3 = new Address();
		address3.setaCode("41333");
		address3.setAdd("北凉.胜3");
		user3.setAddress(address3);
		Favorites favorites3 = new Favorites();
		favorites3.setCites(Arrays.asList("离阳","北莽"));
		favorites3.setMovies(Arrays.asList("囧妈","疯狂的石头"));
		user3.setFavorites(favorites3);
		Comment comment5 = new Comment("太监3", "我哪知道333", new Date());
		Comment comment6 = new Comment("总管3", "emmmmm333", new Date());
		user3.setComments(Arrays.asList(comment5,comment6));
		Collection<User> insertAll = tempelate.insertAll(Arrays.asList(user,user2,user3));
		for (User collects : insertAll) {
			//看一下写进数据库什么样的数据
			System.err.println(collects);
		}
	}
	
	/**
	 * 范围查询：
	 * $eq  ==> 等于
	 * $lt	==>	小于
	 * $gt	==>	大于
	 * $lte ==>	小于等于
	 * $gte ==>	大于等于
	 * $in	==>	判断元素是否在指定的集合范围内
	 * $all ==>	判断数组是否包含某几个元素，无关顺序
	 * $nin ==>	判断元素是否不在指定的集合范围里
	 */
	@Test
	public void findTest1(){
		Query query = new Query();
		Criteria criteriaDefinition = new Criteria();
		//查找user表中name为温不胜，并且favorites中movies 等于 ("囧妈","疯狂的石头") 的数据
		//criteriaDefinition.and("name").is("温不胜").and("favorites.movies").all(Arrays.asList("囧妈","疯狂的石头"));
		//查找favorites中movies 包含 ("北京欢迎您","疯狂的石头")中任意一个 的数据
		//criteriaDefinition.and("favorites.movies").in(Arrays.asList("北京欢迎您","疯狂的石头"));
		//查找favorites中movies 不包含 ("北京欢迎您","疯狂的石头")中任意一个 的数据
		criteriaDefinition.and("favorites.movies").nin(Arrays.asList("疯狂的石头"));
		query.addCriteria(criteriaDefinition);
		List<User> find = tempelate.find(query , User.class);
		for (User user : find) {
			System.err.println(user);
		}
	}
	
	/**
	 * 布尔运算：
	 * $ne	==>	不等于，不匹配参数条件
	 * $not	==>	不匹配结果
	 * $or	==>	有一个条件成立则匹配
	 * $nor	==>	所有条件都不匹配
	 * $and	==>	所有条件都必须匹配
	 * $exists==>判断元素是否存在
	 * 
	 * 使用query包的静态导入方法
	 */
	@Test
	public void findTest2(){
		Query query = new Query();
		
		//查找name 不等于徐凤年
		/*
		 * Criteria criteriaDefinition = new Criteria();
		 * criteriaDefinition.and("name").ne("徐凤年");
		 * query.addCriteria(criteriaDefinition);
		 */
		//查找lenght没有或者lenght小于等于1.75的数据,
		//not()需要和其他范围运算符联合使用,
		//还要注意not()和其他范围运算符联合使用的效果与其他的不一样，如下两个：
		//来自于query包的静态导入
		//query = query(where("lenght").not().gt(1.75));
		//查找文档数据中lenght大于1.75的数据 
		query = query(where("lenght").exists(true).gt(1.75));
		List<User> find = tempelate.find(query , User.class);
		for (User user : find) {
			System.err.println(user);
		}
	}
	
	/**
	 * 这里使用到了投影操作符将限制查询返回的数组字段的内容只包含匹配elemMatch条件的数组元素。
	 * 其实`$elemMatch专门用于查询数组Field中元素是否满足指定的条件
	 */
	@Test
	public void findTest3(){
		Query query = new Query();
		//查找comments对象数组中author包含太监的数据
		//query = query(where("comments").elemMatch(new Criteria().and("author").regex(".*太监*.")));
		//查找comments对象数组中author包含（太监）并且content包含（知道2）的数据
		query = query(where("comments").elemMatch(new Criteria().andOperator(where("author").regex(".*太监2*."),where("content").regex(".*知道2.*"))));
		List<User> find = tempelate.find(query , User.class);
		for (User user : find) {
			System.err.println(user);
		}
	}
	
	
	
	
	
	

}
