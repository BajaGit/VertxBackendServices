package ch.sba.feedback.server;

import io.vertx.ext.web.Router;

public interface RouteProvider {
	public void registerRoutes(Router router);
}
