package microservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import DAO.ProductsDao;

@Path("/deleteItemMicroservices")
public class DeleteItem {
	@Path("/delete")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response delete(MultivaluedMap<String, String> formParam) {
		String itemid = formParam.getFirst("itemid");
		String username = formParam.getFirst("username");
		
		ProductsDao dao = new ProductsDao();
		
		boolean success = dao.deleteItem(itemid, username);
		
		return Response.ok().entity(success).build();
	}
}
