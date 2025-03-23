package lab2.clients;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import lab2.Discovery;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lab2.api.User;
import lab2.api.service.RestUsers;

public class SearchUserClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		if( args.length != 1) {
			System.err.println( "Use: java " + CreateUserClient.class.getCanonicalName() + " query");
			return;
		}

		Discovery discovery = new Discovery(Discovery.DISCOVERY_ADDR);
		discovery.start();
		URI serverURI = discovery.knownUrisOf("UsersService",1)[0];
		String serverUrl = serverURI.toString();
		String query = args[0];
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );
		
		Response r = target.path("/").queryParam( RestUsers.QUERY, query).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			List<User> users = r.readEntity(new GenericType<List<User>>() {});
			System.out.println("Success: (" + users.size() + " users)");
			users.stream().forEach( u -> System.out.println( u));
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
