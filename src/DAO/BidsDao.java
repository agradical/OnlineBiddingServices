package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import DB.DB;
import beans.Bid;
import beans.Bids;

public class BidsDao {
	final static Logger logger = Logger.getLogger(BidsDao.class);

	public boolean postBid(String itemName, String postUserEmail, String bidUserEmail, 
		String itemID, String bidderId, String postUserID, String actPrice) {

		logger.info("DATABASE: Creating new bid for "+itemName);

		boolean result = false;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "INSERT INTO bid (Prod_Id, Bidder_Id, Post_User_Id, Act_Price, Prod_Name, Post_Email, Bidder_Email) "
					+ "VALUES ('"+itemID + "','"+ bidderId + "','" + postUserID + "','" 
					+ actPrice + "','" + itemName + "','"+ postUserEmail + "','" + bidUserEmail + "');";

			Statement stmt1 = conn.createStatement();

			stmt1.executeUpdate(query);
			result = true;
			System.out.println("The new bid is inserted successfully.");

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Bids searchPostedBidsOnItem(String title) {

		logger.info("DATABASE: Search My products: "+title);

		Bids bids = new Bids();
		
		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String sqlcmd1 = "SELECT * FROM user_data.bid WHERE Prod_Id ='" + title + "';";

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(sqlcmd1);

			while (result.next()) {
				Bid b = new Bid();

				b.setId(result.getString("Bid_Id"));
				b.setItemId(result.getString("Prod_Id"));
				b.setUsername(result.getString("Bidder_Id"));
				b.setSellerName(result.getString("Post_User_Id"));
				b.setPrice(result.getString("Act_Price"));
				b.setBuyerEmail(result.getString("Bidder_Email"));
				b.setSellerEmail(result.getString("Post_Email"));

				bids.addBids(b);
			}
			System.out.println("The search product result is got successfully.");
			result.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bids;
	}

}
