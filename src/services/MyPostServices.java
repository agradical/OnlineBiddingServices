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
import beans.Products;
import beans.User;

@Path("/mypostservices")
public class MyPostServices {
	
	final static Logger logger = Logger.getLogger(MyPostServices.class);

	@Path("/view")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response searchProducts(String data) {
				
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
			
		String username = user.getUsername();
		ProductsDao dao = new ProductsDao();
		Products products = dao.getProductsByUser(username);
	
		String response = gson.toJson(products);
		return Response.ok().entity(response).build();
	}
	
}
