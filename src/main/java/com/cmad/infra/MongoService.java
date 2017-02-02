package com.cmad.infra;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class MongoService {
private static Datastore datatstore;

public static Datastore  getDataStore(){
	if(datatstore==null){
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		datatstore= morphia.createDatastore(mongo, "cmad-morphia");

	}
	return datatstore;
}

public static void close(){
	if(datatstore!=null){
		datatstore.getMongo().close();
		datatstore = null;
	}
}
}
