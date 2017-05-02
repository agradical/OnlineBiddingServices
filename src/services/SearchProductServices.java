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

import DAO.ProductsDao;
import auth.AuthKey;
import beans.Products;

@Path("/searchproductservices")
public class SearchProductServices {
	
	final static Logger logger = Logger.getLogger(SearchProductServices.class);

	@Path("/search")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response searchProducts(@Context HttpHeaders headers, MultivaluedMap<String, String> searchParam) {

		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		String search = searchParam.getFirst("search");
		
		ProductsDao dao = new ProductsDao();
		Products products = dao.searchProducts(search);
		
		if(products != null){
			logger.info("Search product title: "+search+": SUCCESS");
		}
		else {
			logger.info("Search product title: "+search+": FAIL");
		}
		Gson gson = new Gson();
		String response = gson.toJson(products);
		return Response.ok().entity(response).build();
	}
	
}
