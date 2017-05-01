package services;

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
import beans.Products;

@Path("/searchproductservices")
public class SearchProductServices {
	
	final static Logger logger = Logger.getLogger(SearchProductServices.class);

	@Path("/search")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response searchProducts(MultivaluedMap<String, String> searchParam) {

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
