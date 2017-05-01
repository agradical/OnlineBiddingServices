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

	public boolean postBid(String name, String userEmail, String bidderEmail, 
		String productId, String bidderId, String userId, String price) {

		logger.info("DATABASE: Creating new bid for "+name);

		boolean result = false;

		try {
			DB db = new DB();
			Connection conn = db.getConnection();

			String query = "INSERT INTO bid (product_id, bidder_id, user_id, price, name, user_email, bidder_email) "
					+ "VALUES ('"+productId + "','"+ bidderId + "','" + userId + "','" 
					+ price + "','" + name + "','"+ userEmail + "','" + bidderEmail + "');";

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

			String sqlcmd1 = "SELECT * FROM bid WHERE product_id ='" + title + "';";

			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery(sqlcmd1);

			while (result.next()) {
				Bid b = new Bid();

				b.setId(result.getString("bid_id"));
				b.setItemId(result.getString("product_id"));
				b.setUsername(result.getString("bidder_id"));
				b.setSellerName(result.getString("user_id"));
				b.setPrice(result.getString("price"));
				b.setBuyerEmail(result.getString("bidder_email"));
				b.setSellerEmail(result.getString("user_email"));

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
