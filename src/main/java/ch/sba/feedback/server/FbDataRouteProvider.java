package ch.sba.feedback.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class FbDataRouteProvider implements RouteProvider {
	
	private final Vertx vertx;
	
	public FbDataRouteProvider(Vertx vertx) {
		this.vertx = vertx;
	}
	
	@Override
	public void registerRoutes(Router router) {
		router.route(HttpMethod.POST, "/feedback")
		.handler(BodyHandler.create());
		router.route("/feedback")
			.method(HttpMethod.POST)
			.handler( rc -> {
				vertx.eventBus().publish("feedback.data.new", rc.getBodyAsString());
				rc.response().end();
			});
	}

}
