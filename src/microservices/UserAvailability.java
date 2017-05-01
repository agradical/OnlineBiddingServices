package microservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import DAO.UserDao;

@Path("/signupmicroservices")
public class UserAvailability {
	
	@Path("/usernameavailability/{username}")
	@GET
	public Response isUsernameAvailable(@PathParam("username") String username) {
		//code here to see if userName exists
		UserDao dao = new UserDao();
		boolean isPresent = false;
		try {
			isPresent = dao.isUser(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok().entity(String.valueOf(isPresent)).build();
	}
}
