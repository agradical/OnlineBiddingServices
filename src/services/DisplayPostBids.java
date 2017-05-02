package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.BidsDao;
import auth.AuthKey;
import beans.Bids;

@Path("/displaypostbids")
public class DisplayPostBids {
	
	final static Logger logger = Logger.getLogger(DisplayPostBids.class);

	@Path("/display")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response postbids(@Context HttpHeaders headers, MultivaluedMap<String, String> postParam) {
				
		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		String itemid = postParam.getFirst("itemid");

		BidsDao dao = new BidsDao();
		Bids bids = dao.searchPostedBidsOnItem(itemid);
		
		Gson gson = new Gson();
		String response = gson.toJson(bids);
		return Response.ok().entity(response).build();
		
	}
}
