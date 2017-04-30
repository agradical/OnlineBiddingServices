package services;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.ProductsDao;
import beans.Product;
import beans.Products;


@Path("/allproducts")
public class AllProductsService {
	
	final static Logger logger = Logger.getLogger(AllProductsService.class);

	
	@Path("/list")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllProducts(String data) {
		
		Products products = new Products();
		ProductsDao dao = new ProductsDao();
		ArrayList<ArrayList<String>> postResult = dao.getAllProducts();
		
		if(postResult != null){
			for(int index=0;index < postResult.size();index++) {
				Product product = new Product();
				
				product.setUsername(postResult.get(index).get(0));
				product.setId(postResult.get(index).get(1));
				product.setName(postResult.get(index).get(2));
				product.setPrice(postResult.get(index).get(3));
				product.setDescription(postResult.get(index).get(4));
				product.setCategory(postResult.get(index).get(5));
				product.setQuality(postResult.get(index).get(6));
				product.setAddress1(postResult.get(index).get(7));
				product.setAddress2(postResult.get(index).get(8));
				product.setCountry(postResult.get(index).get(9));
				product.setState(postResult.get(index).get(10));
				product.setCity(postResult.get(index).get(11));
				product.setEmailId(postResult.get(index).get(12));
				
				products.addProducts(product);
			}
		}
		Gson gson = new Gson();
		String response = gson.toJson(products);
		return Response.ok().entity(response).build();
	}
	
	@Path("/availableusername/{username}")
	@GET
	public String availableUsername(@PathParam("username") String username) {
		//code here to see if userName exists		
		return username + "001";
	}

}
