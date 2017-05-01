package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import DAO.ProductsDao;
import beans.Products;
import beans.User;


@Path("/deleteProductService")
public class DeleteItemService {
	
	@Path("/deletemicro")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response deletemicro(MultivaluedMap<String, String> formParam) {
						
		String username = formParam.getFirst("username");
		
		Client client1 = Client.create();
		WebResource webResource1 = client1.resource("http://localhost:9090/OnlineBiddingMicroServices/rest/deleteItemMicroservices/delete");
		
		ClientResponse restResponse1 = webResource1
			    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
			    .post(ClientResponse.class, formParam);
		
		Boolean.parseBoolean(restResponse1.getEntity(String.class));
		
		User user = new User();
		user.setUsername(username);
		
		Client client2 = Client.create();
		WebResource webResource2 = client2.resource("http://localhost:9090/OnlineBiddingMicroServices/rest/mypostservices/view");
		
		Gson gson = new Gson();
		String data = gson.toJson(user);
		
		ClientResponse restResponse2 = webResource2
			    .type(MediaType.APPLICATION_JSON)
			    .post(ClientResponse.class, data);
		
		if (restResponse2.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + restResponse2.getStatus());
		}

		Products products  = gson.fromJson(restResponse2.getEntity(String.class), Products.class);
		
		return Response.ok().entity(products).build();
	}

	@Path("/delete")
	@POST
	@Consumes("application/x-www-form-urlencoded")
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

