package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.ProductsDao;
import beans.Product;

@Path("/addproductservices")
public class AddProductServices {
	
	final static Logger logger = Logger.getLogger(AddProductServices.class);

	@Path("/newproduct")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response addProduct(String data) {
		boolean success = false;
		Gson gson = new Gson();
		Product user = gson.fromJson(data, Product.class);
		
		String name = user.getName();
		String price = user.getPrice();
		String description = user.getDescription();
		String category = user.getCategory();
		String quality = user.getQuality();
		String address1 = user.getAddress1();
		String address2 = user.getAddress2();
		String country = user.getCountry();
		String state = user.getState();
		String city = user.getCity();
		String username = user.getUsername();

		ProductsDao dao = new ProductsDao();
		success = dao.prodPost(username, name, price, description, category, 
									quality, address1, address2, country, state,city);
		
		if(success){
			logger.info("Bid posting for product: "+name+": SUCCESS");
		}
		else{
			logger.info("Bid posting for product: "+name+": FAIL");
		}
		return Response.ok().entity(String.valueOf(success)).build();
	}
}
