package ch.sba.feedback.server;

import java.util.List;

import org.ektorp.ViewQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ch.sba.feedback.couch.CouchDbFactory;

public class AppDefBuilder {

	public JsonNode build(String id){
		//TODO: will have some active attr - will need to update view
		JsonNode app = CouchDbFactory.INSTANCE.getDb("applications").get(JsonNode.class, id);
		
		ObjectNode result = JsonNodeFactory.instance.objectNode();
		
		String appID = app.get("unid").asText();
		
		result.put("appid", app.get("unid"));
		result.put("name", app.get("name"));
		
		//TODO: involve Versionings?
		ViewQuery query = new ViewQuery().designDocId("_design/feedbackdesign").viewName("by_app").key(appID);
		List<JsonNode> fbDesigns = CouchDbFactory.INSTANCE.getDb("feedbackdesign").queryView(query, JsonNode.class);
		
		result.put("fbDesigns", buildArray(fbDesigns));
		
		
		
		return result;
	}
	
	private ArrayNode buildArray(List<JsonNode> nodes){
		ArrayNode arr = JsonNodeFactory.instance.arrayNode();
		nodes.stream()
			.map(this::attachFormDesign)
			.filter( n -> n.get("form") != null)
			.forEach(arr::add);
		return arr;
	}
	
	private JsonNode attachFormDesign(JsonNode fbDesign){
		JsonNode formIdNode = fbDesign.get("formVersionId");
		if ( formIdNode != null && !formIdNode.asText().equals("null")){
			JsonNode fdNode = CouchDbFactory.INSTANCE.getDb("formdesign")
								.get(JsonNode.class, formIdNode.asText());
			if ( fdNode != null){
				if ( fbDesign.isObject()){
					((ObjectNode)fbDesign).put("form", fdNode);
				}
			}
		}
		return fbDesign;
	}
}
