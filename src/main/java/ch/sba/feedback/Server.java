package ch.sba.feedback;

import ch.sba.feedback.server.AppDefRouteProvider;
import ch.sba.feedback.server.CouchRouteProvider;
import ch.sba.feedback.server.FbDataRouteProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

	@Override
	public void start(){		
		try {
			//HttpServer
		    HttpServer server = vertx.createHttpServer();
		    
		    //start dependent Verticles
		    vertx.deployVerticle("ch.sba.feedback.data.FeedbackDataVerticle");
		    
		    //Routes
		    Router router = Router.router(vertx);
		    
		    //add REST for all couchDBs ( superfluous - couchDB provides this functionality)
		    new CouchRouteProvider().registerRoutes(router);
		    
		    //TODO: aggregate application information into 1 object,
		    //to minimize loading time and call count
		    new AppDefRouteProvider().registerRoutes(router);
		    
		    
		    //TODO: provide endpoint to send feedbackdata to
		    //design with adapters so it can be written to ANY db/engine
		    new FbDataRouteProvider(vertx).registerRoutes(router);
		    
		    //server start
		    server.requestHandler(router::accept).listen(8080);
	    } catch(Exception e){
			e.printStackTrace();
		}
	}
}
