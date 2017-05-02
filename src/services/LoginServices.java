package services;


import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import DAO.UserDao;
import auth.AuthKey;
import beans.User;
 
@Path("/loginservices")
public class LoginServices {
	
	final static Logger logger = Logger.getLogger(LoginServices.class);

	@Path("/checkuservalidity")
	@POST
	@Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
	public Response isValidUser(@Context HttpHeaders headers, MultivaluedMap<String, String> formParam) throws IOException, SQLException, InterruptedException, ExecutionException {
		
		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		String username = formParam.getFirst("username");
		String password = formParam.getFirst("password");
		String location = formParam.getFirst("location");
		
		UserDao dao = new UserDao();
		boolean isValidUser = dao.userLogin(username, password);
		User user = new User();
		if(isValidUser) {
			dao.updateLocation(username, location);
			user = dao.getUser(username);
			logger.info("Login request:"+username+": SUCCESS");
		}
		else {
			logger.info("Login request:"+username+": FAIL");
		}
		Gson gson = new Gson();
		String response = gson.toJson(user);
		return Response.ok().entity(response).build();
		
	}
}