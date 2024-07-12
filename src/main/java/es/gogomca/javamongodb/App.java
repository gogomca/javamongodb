package es.gogomca.javamongodb;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;

public class App {
	private static Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		// We expect the connection string and the database name as properties.
		var connectionString = System.getProperty("connectionString");
		logger.info(String.format("Connection string: %s", connectionString));

		var databaseName = System.getProperty("databaseName");
		logger.info(String.format("Database name: %s", databaseName));

		var mongoConnectionString = new ConnectionString(connectionString);

		// Generate the API to connect to Atlas.
		var serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();

		// Generate the setting to create the client to connect to Atlas.
		var settings = MongoClientSettings.builder().applyConnectionString(mongoConnectionString).serverApi(serverApi).build();

		// Create a new client and connect to Atlas.
		try (var mongoClient = MongoClients.create(settings)) {
			try {
				// Send a ping to confirm a successful connection to one of the existing databases.
				var database = mongoClient.getDatabase(databaseName);
				database.runCommand(new Document("ping", 1));
				logger.info("Pinged your deployment. You successfully connected to MongoDB!");
			} catch (MongoException e) {
				e.printStackTrace();
				return;
			}

			var databases = mongoClient.listDatabases().into(new ArrayList<>());
			databases.forEach(db -> logger.info(db.toJson()));
		}
	}

}
