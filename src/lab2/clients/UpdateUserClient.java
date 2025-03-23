package lab2.clients;

import java.io.IOException;
import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lab2.Discovery;
import lab2.api.User;
import lab2.api.service.RestUsers;
import org.glassfish.jersey.client.ClientConfig;

public class UpdateUserClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		if( args.length != 5) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " userId oldpwd fullName email password");
			return;
		}

		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();
		URI serverURI = discovery.knownUrisOf("UsersService",1)[0];
		String serverUrl = serverURI.toString();
		String userId = args[0];
		String oldpwd = args[1];
		String fullName = args[2];
		String email = args[3];
		String password = args[4];
		
		User usr = new User( userId, fullName, email, password);
		
		System.out.println("Sending request to server.");
		
		//TODO complete this client code
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );

		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, oldpwd).request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(usr, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity() )
			System.out.println("Success, updated user: " + r.readEntity(String.class) );
		else
			System.out.println("Error, HTTP error status: " + r.getStatus() );
	}
	
}
