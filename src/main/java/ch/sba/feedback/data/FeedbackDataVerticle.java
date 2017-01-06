package ch.sba.feedback.data;

import com.fasterxml.jackson.databind.JsonNode;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;

public class FeedbackDataVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		System.out.println("start fb data vert");
		
		MessageConsumer<JsonNode> consumer = vertx.eventBus().consumer("feedback.data.new");
		
		consumer.handler( msg -> {
			System.out.println(msg.body());
		});
	}
}
