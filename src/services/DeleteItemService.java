package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


import com.google.gson.Gson;

import DAO.ProductsDao;
import beans.Products;


@Path("/deleteProductService")
public class DeleteItemService {
	
	@Path("/delete")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response delete(MultivaluedMap<String, String> formParam) {
						
		String itemid = formParam.getFirst("itemid");
		String username = formParam.getFirst("username");
		
		ProductsDao dao = new ProductsDao();
		dao.deleteItem(itemid, username);
		Products products = dao.getProductsByUser(username);
		
		Gson gson = new Gson();
		String response = gson.toJson(products);
		return Response.ok().entity(response).build();
	}

}

