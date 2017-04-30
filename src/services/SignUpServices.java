package services;

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

import DAO.UserDao;
import beans.User;

@Path("/signupservices")
public class SignUpServices {
	
	final static Logger logger = Logger.getLogger(SignUpServices.class);
	
	@Path("/newuser")
	@POST
	@Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
	public Response addNewUser(String data) {
		boolean success = false; //should be set to false
		
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
		
		UserDao dao = new UserDao();
		success = dao.userSignUp(username, password, firstName, lastName, emailAddress, address1,address2,city,state,country,dateofbirth, phone, gender);
		
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
