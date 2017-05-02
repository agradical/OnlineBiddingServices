package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import DAO.UserDao;
import auth.AuthKey;
import beans.User;

@Path("/signupservices")
public class SignUpServices {
	
	final static Logger logger = Logger.getLogger(SignUpServices.class);
	
	@Path("/newusermicro")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response addNewUserMicro(@Context HttpHeaders headers, String data) {
		

		if(headers.getRequestHeader("secret") == null || !headers.getRequestHeader("secret").get(0).equals(AuthKey.KEY+"")) {
			System.out.println(headers.getRequestHeader("secret"));
			logger.info("Login Bad request without AuthKey");
			return Response.status(302).entity("Unauthorized access").build();
		}
		
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		
		String username = user.getUsername();

		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:9090/OnlineBiddingMicroServices/rest/signupmicroservices/usernameavailability/"+username);
		ClientResponse restResponse = webResource
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		
		if (restResponse.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
		}

		String statusString = restResponse.getEntity(String.class);
		boolean isusernamepresent = Boolean.parseBoolean(statusString);
		
		if(!isusernamepresent) {
			
			webResource = client.resource("http://localhost:9090/OnlineBiddingMicroServices/rest/signupservices/newuser");
			
			Gson userJson = new Gson();
			String user_data = userJson.toJson(user);

			restResponse = webResource
								.type(MediaType.APPLICATION_JSON)
								.post(ClientResponse.class, user_data);
			
			if (restResponse.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
			}
 
			statusString = restResponse.getEntity(String.class);
			return Response.ok().entity(statusString).build();		
		}
		return Response.ok().entity(String.valueOf(false)).build();
	}
	
	@Path("/newuser")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response addNewUser(String data) {
		boolean success = false;
		
		Gson gson = new Gson();
		User user = gson.fromJson(data, User.class);
		
		String username = user.getUsername();
		String password = user.getPassword();
		
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		
		String emailAddress = user.getEmail();
		String phone = user.getPhone();
		String gender = user.getGender();
		String dateofbirth = user.getDateofbirth();

		String address1 = user.getAddress1();
		String address2 = user.getAddress2();
		String city = user.getCity();
		String state = user.getState();
		String country = user.getCountry();
		String location = user.getLocation();
		
		UserDao dao = new UserDao();
		success = dao.userSignUp(username, password, firstName, lastName, emailAddress, 
				address1,address2,city,state,country,dateofbirth, phone, gender, location);
		
		if(success){
			logger.info("Signup request: "+username+": SUCCESS");
		}
		else {
			logger.info("Signup request:"+username+": FALSE");
		}
		return Response.ok().entity(String.valueOf(success)).build();
	}
	
	@Path("/usernameavailability/{username}")
	@GET
	public Response isUsernameAvailable(@PathParam("username") String username) {
		//code here to see if userName exists
		logger.info("Username: "+username+" :availibility check");
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
