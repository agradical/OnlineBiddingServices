package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

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

			String query = "SELECT * FROM user_data.users WHERE Username='" + username + "';";

			ResultSet result = stmt.executeQuery(query);
			conn.close();
			if(result.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean userLogin(String username, String password) {
		boolean result = false;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			logger.info("DATABASE: Attempting login for username: "+username);

			CachedRowSetImpl crsi = new CachedRowSetImpl();
			MemcachedClient memcache = new MemcachedClient(new BinaryConnectionFactory(), 
					AddrUtil.getAddresses("localhost:11211"));
			Object object1 = null;

			String query = "SELECT * FROM user_data.users WHERE Username='" + username + "';";

			String md5 = db.MD5encode(query);
			Future<Object> f = memcache.asyncGet(md5);

			try {
				object1 = f.get(5, TimeUnit.SECONDS);
			}
			catch (TimeoutException e) {
				f.cancel(false);
				System.out.println("Memcached timeout...");
			}

			Statement stmt1 = conn.createStatement();
			if (object1==null) {
				logger.info("MEMCACHE: Cache Miss");
				ResultSet result1 = stmt1.executeQuery(query);
				crsi.populate(result1);
				result1.close();
				memcache.set(query, 60*60*24*30, crsi);
				if (crsi.next()) {
					if (crsi.getString("Pass").equals(password)) {
						result = true;
					} else {
						result = false;
						int failedLoginNum = Integer.parseInt(crsi.getString("No_failed_login"));
						failedLoginNum++;
						String sqlcmd3 = "UPDATE users SET No_failed_login='" + failedLoginNum 
								+ "' WHERE Username ='" + username + "';";
						stmt1.executeUpdate(sqlcmd3);
						System.out.println("The sql statement is " + sqlcmd3);
						System.out.println("Login failed. Wrong password.");
					}
				} else {
					result = false;
					System.out.println("The user doesn't exist.");
				}
				crsi.close();
			}
			else  {
				logger.info("MEMCACHE: Cache Hit");
				crsi = (CachedRowSetImpl)object1;
				crsi.beforeFirst();
				if (crsi.next()) {
					if (crsi.getString("Pass").equals(password)) {
						result = true;
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String Last_login = dateFormat.format(new Date());
						String sqlcmd2 = "UPDATE users SET Last_login='" + Last_login + "' WHERE Username ='" + username + "';";
						stmt1.executeUpdate(sqlcmd2);
						System.out.println("The sql statement is " + sqlcmd2);
						System.out.println("This is a valid login.");
					} else {
						result = false;
						int failedLoginNum = Integer.parseInt(crsi.getString("No_failed_login"));
						failedLoginNum++;
						String sqlcmd3 = "UPDATE users SET No_failed_login='" + failedLoginNum + "' WHERE Username ='" + username + "';";
						stmt1.executeUpdate(sqlcmd3);
						System.out.println("The sql statement is " + sqlcmd3);
						System.out.println("Login failed. Wrong password.");
					}
				} else {
					result = false;
					System.out.println("The user doesn't exist.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public User getUser(String username) {

		logger.info("DATABASE: Getting profile for username: "+username);
		User user = new User();

		try {
			String query = "SELECT * FROM user_data.users WHERE Username='" + username + "';";
			DB db = new DB();
			Connection conn = db.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {
				result.getString("Email_Id");
				break;
			}
			user.setUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean updateUser(String username, String password, String firstName, String lastName,
			String email, String address1,String address2,String city, String state,
			String country, String dateofbirth, String phone, String gender) {

		logger.info("DATABASE: Updating user: "+username);
		
		boolean result = false;
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();
			String query1 = "SELECT * FROM user_data.users WHERE Username='" + username + "';";

			Statement stmt1 = conn.createStatement();
			ResultSet result1 = stmt1.executeQuery(query1);
			if (result1.next()) {
				result = true;
				String query2 = "UPDATE users SET U_First_Name='" + firstName + "',Address_Line1='" 
						+ address1 + "',Address_Line1='" + address2 + "',U_Last_Name='" + lastName 
						+ "',Pass='" + password + "',Email_Id='" + email + "',Birth_Date='" 
						+ dateofbirth + "',Gender='" + gender + "',City='" + city + "',State='" 
						+ state + "',Country='" + country + "',Ph_No='" + phone + "' "
						+ "WHERE Username ='" + username + "';";
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

	public boolean updateUserAddress(String username, String newAddress) {

		logger.info("DATABASE: Update user address: "+newAddress+": username: "+username);

		boolean result = false;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "UPDATE USERS SET Address='" + newAddress + "' WHERE Username='" + username + "';";
			Statement stmt = conn.createStatement();

			stmt.executeUpdate(query);
			result = true;

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateUserPhone(String username, String newPhone) {
		logger.info("DATABASE: Update user phone: "+newPhone+": username: "+username);

		boolean result = false;
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "UPDATE USERS SET Phone='" + newPhone + "' WHERE Username='" + username + "';";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);

			result = true;
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
			String query = "SELECT * FROM user_data.users WHERE Username='" + username + "';";
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				response = false;
				System.out.println("Sign up failed. The username already exists.");
			} else {
				response = true;
				int noFailedLogin = 0;
				String query2 = "INSERT INTO users VALUES ('" + username + "','" + firstName + "','" 
						+ lastName + "','" + password + "','" + email + "','" + dateofbirth 
						+ "','" + gender + "','" + city + "','" + state + "','" + country 
						+ "','" + phone + "','" + address1 + "','" + address2 + "',"  + null 
						+ ",'" + noFailedLogin + "'," + null + ");";
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
