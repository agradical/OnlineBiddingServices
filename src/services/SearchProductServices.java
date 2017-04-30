package services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.ProductsDao;
import beans.Product;
import beans.Products;

@Path("/searchproductservices")
public class SearchProductServices {
	
	final static Logger logger = Logger.getLogger(SearchProductServices.class);

	@Path("/search")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response searchProducts(MultivaluedMap<String, String> searchParam) {

		Products products = new Products();
		String search = searchParam.getFirst("search");
		
		ProductsDao dao = new ProductsDao();
		ArrayList<ArrayList<String>> searchResult = dao.searchProducts(search);
		
		if(searchResult != null){
			for(int index=0;index < searchResult.size();index++) {
				
				Product product = new Product();
				product.setUsername(searchResult.get(index).get(0));
				product.setId(searchResult.get(index).get(1));
				product.setName(searchResult.get(index).get(2));
				product.setPrice(searchResult.get(index).get(3));
				product.setDescription(searchResult.get(index).get(4));
				product.setCategory(searchResult.get(index).get(5));
				product.setQuality(searchResult.get(index).get(6));
				product.setAddress1(searchResult.get(index).get(7));
				product.setAddress2(searchResult.get(index).get(8));
				product.setCountry(searchResult.get(index).get(9));
				product.setState(searchResult.get(index).get(10));
				product.setCity(searchResult.get(index).get(11));
				product.setEmailId(searchResult.get(index).get(12));
				
				products.addProducts(product);
			}
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
