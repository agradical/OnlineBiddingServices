package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.BidsDao;
import auth.AuthKey;
import beans.Bid;


@Path("/bidservices")
public class BidServices {
	
	final static Logger logger = Logger.getLogger(BidServices.class);

	@Path("/newbid")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response postNewBid(@Context HttpHeaders headers, String data) {
		
		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		boolean response = false;
		boolean success = false;
		Gson gson = new Gson();
		Bid bid = gson.fromJson(data, Bid.class);

		String itemName = bid.getItemName();
		String sellerEmail = bid.getSellerEmail();
		String buyerEmail = bid.getBuyerEmail();
		String itemId = bid.getItemId();
		String buyerName = bid.getUsername();
		String sellerName = bid.getSellerName();
		String price = bid.getPrice();
		
		BidsDao dao = new BidsDao();
		success = dao.postBid(itemName, sellerEmail, buyerEmail, itemId, buyerName, sellerName, price);
		System.out.println(success);		
		
		if(success){
			response = true;
			logger.info("Register new bid SUCCESS");
		}
		else{
			response = false;
			logger.info("Register new bid FAIL");
		}
		return Response.ok().entity(String.valueOf(response)).build();
	}
}
