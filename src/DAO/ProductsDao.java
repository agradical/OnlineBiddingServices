package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import DB.DB;
import beans.Product;
import beans.Products;

public class ProductsDao {
	final static Logger logger = Logger.getLogger(ProductsDao.class);
	
	public boolean prodPost(String username, String itemName, String itemPrice,String itemDesc,
			String itemCategory,String itemQuality,String add1, String add2,String country,
			String state,String city) {

		logger.info("DATABASE: Creating new sell post by: "+username);
		
		boolean result = false;
		
		try {
			DB db = new DB();
			Connection conn =  db.getConnection();

			SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
			Calendar calendar = Calendar.getInstance();
			java.sql.Date ourJavaDateObject = new java.sql.Date(calendar.getTime().getTime());
			calendar.add( Calendar.DATE, +3); 
			String convertedDate=dateFormat.format(calendar.getTime()); 

			String query = "INSERT INTO product VALUES (" + null + ",'" + username + "','" 
					+ itemName + "','" + itemPrice + "','" + itemDesc + "','" 
					+ itemCategory + "','" + itemQuality + "','" + add1 + "','" 
					+ add2 + "','" + city + "','" + state + "','" + country + "','" 
					+ ourJavaDateObject + "','" + convertedDate +"');";

			Statement stmt1 = conn.createStatement();

			stmt1.executeUpdate(query);
			result = true;
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ArrayList<String>> getAllProducts() {

		ArrayList<ArrayList<String>> searchResult = null;
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String sqlcmd1 = "SELECT Email_Id,Post_User_Id, Prod_Id, Prod_Name, P_Price,"
					+ " P_Description, P_Category, P_Quality, P_Address_Line1, P_Address_Line2,"
					+ " P_City, P_State, P_Country"
					+ " FROM user_data.product,user_data.users"
					+ " WHERE Post_User_Id=Username;";

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(sqlcmd1);

			searchResult = new ArrayList<ArrayList<String>>();

			while (result.next()) {
				ArrayList<String> currProduct = new ArrayList<String>();

				currProduct.add(result.getString("Post_User_Id"));
				currProduct.add(result.getString("Prod_Id"));
				currProduct.add(result.getString("Prod_Name"));
				currProduct.add(result.getString("P_Price"));
				currProduct.add(result.getString("P_Description"));
				currProduct.add(result.getString("P_Category"));
				currProduct.add(result.getString("P_Quality"));
				currProduct.add(result.getString("P_Address_Line1"));
				currProduct.add(result.getString("P_Address_Line2"));
				currProduct.add(result.getString("P_City"));
				currProduct.add(result.getString("P_State"));
				currProduct.add(result.getString("P_Country"));
				currProduct.add(result.getString("Email_Id"));
				searchResult.add(currProduct);

			}
			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResult;
	}

	public Products getProductsByUser(String username) {
		Products products = new Products();

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "SELECT Prod_Id, Prod_Name, P_Price, P_Description,"
					+ " P_Category, P_Quality, P_Address_Line1, P_Address_Line2, "
					+ "P_City, P_State, P_Country,Email_Id  "
					+ "FROM user_data.product,users "
					+ "WHERE Post_User_Id ='" + username + "' AND Post_User_Id= Username;" ;

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(query);	// get the result
			System.out.println("The sql statement is " + query);

			while (result.next()) {

				Product p = new Product();

				p.setId(result.getString("Prod_Id"));
				p.setName(result.getString("Prod_Name"));
				p.setPrice(result.getString("P_Price"));
				p.setDescription(result.getString("P_Description"));
				p.setCategory(result.getString("P_Category"));
				p.setQuality(result.getString("P_Quality"));
				p.setAddress1(result.getString("P_Address_Line1"));
				p.setAddress2(result.getString("P_Address_Line2"));
				p.setCity(result.getString("P_City"));
				p.setState(result.getString("P_State"));
				p.setCountry(result.getString("P_Country"));
				p.setEmailId(result.getString("Email_Id"));

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

			String query = "DELETE FROM user_data.product "
					+ "WHERE Prod_Id='" + itemid + "' AND Post_User_Id='" + username + "';";

			Statement stmt = conn.createStatement();

			stmt.executeUpdate(query);
			result = true;
			conn.close();
		} catch(Exception e) {
			e.printStackTrace(); 
		}
		return result;
	}

	public ArrayList<ArrayList<String>> searchProducts(String text) {

		logger.info("DATABASE: Search for products1: "+text);
		
		ArrayList<ArrayList<String>> searchResult = null;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "SELECT * FROM user_data.product,users "
					+ "WHERE Prod_Name LIKE '%" + text + "%' AND Post_User_Id = Username;";

			Statement stmt1 = conn.createStatement();

			ResultSet result = stmt1.executeQuery(query);
			System.out.println("The sql statement is " + query);

			searchResult = new ArrayList<ArrayList<String>>();
			while (result.next()) {
				ArrayList<String> currProduct = new ArrayList<String>();
				currProduct.add(result.getString("Post_User_Id"));
				currProduct.add(result.getString("Prod_Id"));
				currProduct.add(result.getString("Prod_Name"));
				currProduct.add(result.getString("P_Price"));
				currProduct.add(result.getString("P_Description"));
				currProduct.add(result.getString("P_Category"));
				currProduct.add(result.getString("P_Quality"));
				currProduct.add(result.getString("P_Address_Line1"));
				currProduct.add(result.getString("P_Address_Line2"));
				currProduct.add(result.getString("P_City"));
				currProduct.add(result.getString("P_State"));
				currProduct.add(result.getString("P_Country"));
				currProduct.add(result.getString("Email_Id"));

				searchResult.add(currProduct);
			}
			System.out.println("The search result is got successfully.");

			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResult;
	}

}
