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

import DAO.ProductsDao;
import auth.AuthKey;
import beans.Product;

@Path("/addproductservices")
public class AddProductServices {
	
	final static Logger logger = Logger.getLogger(AddProductServices.class);

	@Path("/newproduct")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(@Context HttpHeaders headers, String data) {
		
		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		
		boolean success = false;
		Gson gson = new Gson();
		Product product = gson.fromJson(data, Product.class);
		
		
		
		String name = product.getName();
		String price = product.getPrice();
		String description = product.getDescription();
		String category = product.getCategory();
		String quality = product.getQuality();
		String address1 = product.getAddress1();
		String address2 = product.getAddress2();
		String country = product.getCountry();
		String state = product.getState();
		String city = product.getCity();
		String username = product.getUsername();
		String minutes = product.getMinutes();

		ProductsDao dao = new ProductsDao();
		success = dao.prodPost(username, name, price, description, category, 
									quality, address1, address2, country, state,city,minutes);
		
		if(success){
			logger.info("Bid posting for product: "+name+": SUCCESS");
		}
		else{
			logger.info("Bid posting for product: "+name+": FAIL");
		}
		return Response.ok().entity(String.valueOf(success)).build();
	}
}
