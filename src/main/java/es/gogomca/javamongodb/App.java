package es.gogomca.javamongodb;

import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Hello world!
 *
 */
public class App {
	private static Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		// We expect the password as the first execution argument.
		String user = args[0];
		String password = args[1];

		logger.info(String.format("Credentials: %s - %s", user, password));

		// Generate the connection string to MongoDb database instance in Atlas.
		var connectionStringTemplate = "mongodb+srv://%s:%s@cluster0.k8t8jwp.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
		var connectionString = connectionStringTemplate.formatted(user, password);
		var mongoConnectionString = new ConnectionString(connectionString);

		// Generate the API to connect to Atlas.
		var serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();

		// Generate the setting to create the client to connect to Atlas.
		var settings = MongoClientSettings.builder().applyConnectionString(mongoConnectionString).serverApi(serverApi).build();

		// Create a new client and connect to Atlas.
		try (MongoClient mongoClient = MongoClients.create(settings)) {
			try {
				// Send a ping to confirm a successful connection to one of the existing databases from samples.
				MongoDatabase database = mongoClient.getDatabase("sample_analytics");
				database.runCommand(new Document("ping", 1));
				logger.info("Pinged your deployment. You successfully connected to MongoDB!");
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
	}

}
