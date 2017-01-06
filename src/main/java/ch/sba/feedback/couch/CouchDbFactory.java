package ch.sba.feedback.couch;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public enum CouchDbFactory  {
	INSTANCE;
	
	private HttpClient client;
	
	private CouchDbInstance couch;
	
	
	private CouchDbFactory() {
		if ( client == null){
			try{
				client = new StdHttpClient.Builder()
						.url("http://192.168.230.136:5984")
						.username("admin")
						.password("admin")
						.build();
				couch = new StdCouchDbInstance(client);
			} catch( Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public CouchDbConnector getDb(String name){
		return getDb(name, true);
	}
	
	public CouchDbConnector getDb(String name, boolean createOnFail){
		return couch.createConnector(name, createOnFail);
	}
	
	public List<String> getDbNames(){
		return couch.getAllDatabases();
	}
	
}
