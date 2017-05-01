package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import DB.DB;
import beans.User;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

public class UserDao {
	final static Logger logger = Logger.getLogger(UserDao.class);

	public boolean isUser(String username) {
		logger.info("DATABASE: Checking if username: "+username+": present");
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();

			String query = "SELECT * FROM users WHERE username='" + username + "';";

			ResultSet result = stmt.executeQuery(query);
			
			if(result.next()) {
				return true;
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean userLogin(String username, String password) {
		boolean success = false;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			logger.info("DATABASE: Attempting login for username: "+username);

			MemcachedClient memcache = new MemcachedClient(new BinaryConnectionFactory(), 
											AddrUtil.getAddresses("localhost:11211"));
			Object object1 = null;

			String query = "SELECT * FROM users WHERE username='" + username + "';";

			String md5 = db.MD5encode(query);
			Future<Object> f = memcache.asyncGet(md5);

			try {
				object1 = f.get(5, TimeUnit.SECONDS);
			}
			catch (TimeoutException e) {
				f.cancel(false);
				System.out.println("Memcached timeout...");
			}

			Statement stmt = conn.createStatement();
			if (object1==null) {
				logger.info("MEMCACHE: Cache Miss");
				ResultSet result = stmt.executeQuery(query);
				memcache.set(md5, 60*60*24*30, true);
				result.close();
			}
			else  {
				logger.info("MEMCACHE: Cache Hit");
			}
			success = true;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}

		return success;
	}

	public User getUser(String username) {

		logger.info("DATABASE: Getting profile for username: "+username);
		User user = new User();

		try {
			String query = "SELECT * FROM users WHERE username='" + username + "';";
			DB db = new DB();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				user.setUsername(result.getString("username"));
				user.setEmail(result.getString("email"));
				user.setAddress1(result.getString("address1"));
				user.setAddress2(result.getString("address2"));
				user.setCity(result.getString("city"));
				user.setState(result.getString("state"));
				user.setCountry(result.getString("country"));
				user.setFirstName(result.getString("firstname"));
				user.setLastName(result.getString("lastname"));
				user.setGender(result.getString("gender"));
				user.setDateofbirth(result.getString("dob"));
				user.setPhone(result.getString("phone"));
				user.setPassword(result.getString("password"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean updateUser(String username,String address1,String address2,String city, String state, String country) {

		logger.info("DATABASE: Updating user: "+username);
		
		boolean result = false;
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();
			String query1 = "SELECT * FROM users WHERE username='" + username + "';";

			Statement stmt1 = conn.createStatement();
			ResultSet result1 = stmt1.executeQuery(query1);
			if (result1.next()) {
				result = true;
				String query2 = "UPDATE users SET  address1='" + address1 + "',address2='" + address2 
						+ "',city='" + city + "',state='" + state + "',country='" + country
						+ "' WHERE username ='" + username + "';";
				stmt1.executeUpdate(query2);
				System.out.println("The user updated information successfully.");
			} else {
				result = true;
				System.out.println("The username does not exists.");
			}

			result1.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public boolean userSignUp(String username, String password, String firstName, String lastName, 
			String email, String address1,String address2,String city, String state,
			String country, String dateofbirth, String phone, String gender) {

		logger.info("DATABASE: Registering new user: "+username);

		boolean response = false;
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();
			String query = "SELECT * FROM users WHERE username='" + username + "';";
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				response = false;
				System.out.println("Sign up failed. The username already exists.");
			} else {
				response = true;
				String query2 = "INSERT INTO users VALUES ('" + username + "','" + firstName + "','" 
						+ lastName + "','" + password + "','" + email + "','" + dateofbirth 
						+ "','" + gender + "','" + city + "','" + state + "','" + country 
						+ "','" + phone + "','" + address1 + "','" + address2 + "');";
				stmt.executeUpdate(query2);
			}
			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
