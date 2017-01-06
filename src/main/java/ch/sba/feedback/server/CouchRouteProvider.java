package ch.sba.feedback.server;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import ch.sba.feedback.couch.CouchDbFactory;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class CouchRouteProvider implements RouteProvider {

	private List<CouchDbConnector> dbs = null;
	
	public CouchRouteProvider(){
		dbs = new ArrayList<>();
		for ( String name : CouchDbFactory.INSTANCE.getDbNames()){
			dbs.add(CouchDbFactory.INSTANCE.getDb(name));
		}
	}
	
	@Override
	public void registerRoutes(Router router) {
		for ( CouchDbConnector db: dbs){
			router.route("/" + db.getDatabaseName())
				.method(HttpMethod.GET)
				.handler( rc -> {
					rc.response().putHeader("content-type", "application/json").end(allDocs(db).toString());
				});
			router.route("/" + db.getDatabaseName() + "/:id")
				.method(HttpMethod.GET)
				.handler( rc -> {
					String docId = rc.pathParams().get("id");
					rc.response().putHeader("content-type", "application/json").end(db.get(JsonNode.class, docId).toString());
				});
		}

	}
	
	private JsonNode allDocs(CouchDbConnector db){
		ArrayNode parentArr = JsonNodeFactory.instance.arrayNode();
		
		for ( String id: db.getAllDocIds()){
			parentArr.add(db.get(JsonNode.class, id));
		}
		
		return parentArr;
	}

}
