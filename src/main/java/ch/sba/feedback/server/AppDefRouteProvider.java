package ch.sba.feedback.server;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class AppDefRouteProvider implements RouteProvider {

	private AppDefBuilder builder = new AppDefBuilder();
	
	@Override
	public void registerRoutes(Router router) {
		router.route("/appdef/:id")
				.method(HttpMethod.GET)
				.handler( rc -> {
					String appid = rc.pathParam("id");
					rc.response()
						.putHeader("content-type", "application/json")
						.end(builder.build(appid).toString());
				});

	}

}
