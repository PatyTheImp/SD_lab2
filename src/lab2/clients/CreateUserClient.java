package lab2.clients;

import java.io.IOException;
import java.net.URI;

import lab2.Discovery;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lab2.api.User;
import lab2.api.service.RestUsers;

public class CreateUserClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		if( args.length != 4) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " userId fullName email password");
			return;
		}

		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();
		URI serverURI = discovery.knownUrisOf("UsersService",1)[0];
		String serverUrl = serverURI.toString();
		String userId = args[0];
		String fullName = args[1];
		String email = args[2];
		String password = args[3];
		
		User usr = new User( userId, fullName, email, password);
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );
		
		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(usr, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			System.out.println("Success, created user with id: " + r.readEntity(String.class) );
		else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
