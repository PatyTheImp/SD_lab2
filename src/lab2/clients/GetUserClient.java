package lab2.clients;

import java.io.IOException;
import java.net.URI;

import lab2.Discovery;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lab2.api.User;
import lab2.api.service.RestUsers;

public class GetUserClient {


	public static void main(String[] args) throws IOException, InterruptedException {
		
		if( args.length != 2) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " userId password");
			return;
		}

		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();
		URI serverURI = discovery.knownUrisOf("UsersService",1)[0];
		String serverUrl = serverURI.toString();
		String userId = args[0];
		String password = args[1];
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );
		
		Response r = target.path( userId )
				.queryParam(RestUsers.PASSWORD, password).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			User usr = r.readEntity(User.class);
			System.out.println( "User : " + usr);
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
