package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import DB.DB;
import beans.Product;
import beans.Products;

public class ProductsDao {
	final static Logger logger = Logger.getLogger(ProductsDao.class);
	
	public boolean prodPost(String username, String name, String price,String description,
			String category,String quality,String address1, String address2, String country,
			String state,String city,String minutes_str) {

		logger.info("DATABASE: Creating new sell post by: "+username);
		
		boolean result = false;
		
		try {
			DB db = new DB();
			Connection conn =  db.getConnection();
			
			Integer minutes = Integer.parseInt(minutes_str);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			
			Date currdate = new Date(calendar.getTimeInMillis());
			String date_str = dateFormat.format(currdate);
			
			long t = calendar.getTimeInMillis();
			Date expiry = new Date(t + (minutes * 60000));
			String expiry_date_str = dateFormat.format(expiry);
			
			
			String query = "INSERT INTO product VALUES (" + null + ",'" + username + "','" 
					+ name + "','" + price + "','" + description + "','" 
					+ category + "','" + quality + "','" + address1 + "','" 
					+ address2 + "','" + city + "','" + state + "','" + country + "','" 
					+ date_str + "','" + expiry_date_str +"');";
			
			System.out.println(query);
			Statement stmt1 = conn.createStatement();

			stmt1.executeUpdate(query);
			result = true;
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Products getAllProducts() {

		Products products = new Products();
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "SELECT u.email, p.user_id, p.product_id, p.name, p.price,"
					+ " p.description, p.category, p.quality, u.address1, u.address2,"
					+ " u.city, u.state, u.country, p.bid_time, p.expirytime "
					+ " FROM product p , users u"
					+ " WHERE p.user_id=u.username;";

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(query);

			while (result.next()) {

				Product p = new Product();
				p.setAddress1(result.getString("address1"));
				p.setAddress2(result.getString("address2"));
				p.setUsername(result.getString("user_id"));
				p.setId(result.getString("product_id"));
				p.setName(result.getString("name"));
				p.setPrice(result.getString("price"));
				p.setDescription(result.getString("description"));
				p.setCategory(result.getString("category"));
				p.setQuality(result.getString("quality"));
				p.setCity(result.getString("city"));
				p.setState(result.getString("state"));
				p.setCountry(result.getString("country"));
				p.setEmailId(result.getString("email"));
				p.setBidTime(result.getString("bid_time"));
				p.setExpiryTime(result.getString("expirytime"));
				
				products.addProducts(p);

			}
			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}

	public Products getProductsByUser(String username) {
		Products products = new Products();

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "SELECT p.product_id, p.name, p.price, p.description,"
					+ " p.category, p.quality, u.address1, u.address2, "
					+ " u.city, u.state, u.country, u.email, p.bid_time, p.expirytime  "
					+ " FROM product p, users u "
					+ " WHERE p.user_id ='" + username + "' AND p.user_id=u.username;" ;

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(query);	// get the result
			System.out.println("The sql statement is " + query);

			while (result.next()) {

				Product p = new Product();

				p.setId(result.getString("product_id"));
				p.setName(result.getString("name"));
				p.setPrice(result.getString("price"));
				p.setDescription(result.getString("description"));
				p.setCategory(result.getString("category"));
				p.setQuality(result.getString("quality"));
				p.setAddress1(result.getString("address1"));
				p.setAddress2(result.getString("address2"));
				p.setCity(result.getString("city"));
				p.setState(result.getString("state"));
				p.setCountry(result.getString("country"));
				p.setEmailId(result.getString("email"));
				p.setUsername(username);
				p.setBidTime(result.getString("bid_time"));
				p.setExpiryTime(result.getString("expirytime"));
				
				products.addProducts(p);

			}
			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}

	public boolean deleteItem(String itemid, String username) {

		boolean result = false;
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "DELETE FROM product "
					+ "WHERE product_id='" + itemid + "' AND user_id='" + username + "';";

			Statement stmt = conn.createStatement();

			stmt.executeUpdate(query);
			result = true;
			conn.close();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		return result;
	}

	public Products searchProducts(String text) {

		logger.info("DATABASE: Search for products1: "+text);
		
		Products products = new Products();
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "SELECT * FROM product p, users u "
					+ " WHERE p.name LIKE '%" + text + "%' AND p.user_id = u.username;";

			Statement stmt1 = conn.createStatement();

			ResultSet result = stmt1.executeQuery(query);
			System.out.println("The sql statement is " + query);


			while (result.next()) {

				Product p = new Product();
				
				p.setAddress1(result.getString("address1"));
				p.setAddress2(result.getString("address2"));
				p.setUsername(result.getString("user_id"));
				p.setId(result.getString("product_id"));
				p.setName(result.getString("name"));
				p.setPrice(result.getString("price"));
				p.setDescription(result.getString("description"));
				p.setCategory(result.getString("category"));
				p.setQuality(result.getString("quality"));
				p.setAddress1(result.getString("address1"));
				p.setAddress2(result.getString("address2"));
				p.setCity(result.getString("city"));
				p.setState(result.getString("state"));
				p.setCountry(result.getString("country"));
				p.setEmailId(result.getString("email"));
				p.setBidTime(result.getString("bid_time"));
				p.setExpiryTime(result.getString("expirytime"));
				
				products.addProducts(p);

			}
			System.out.println("The search result is got successfully.");

			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}

}
