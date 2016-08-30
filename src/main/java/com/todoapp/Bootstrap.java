package com.todoapp;

import com.mongodb.*;

import static spark.Spark.*;



/**
 * Created by nohorbee on 26/08/16.
 */
public class Bootstrap {

    private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8080;

    public static void main(String...args) throws Exception {

        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        staticFileLocation("/public");
        new TodoResource(new TodoService(mongo()));

    }

    private static DB mongo() throws Exception {
        String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        if (host == null) {
            return new MongoClient("localhost").getDB("todoapp");
        }

        int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String dbname = System.getenv("OPENSHIFT_APP_NAME");
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient client = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        client.setWriteConcern(WriteConcern.SAFE);
        DB db = client.getDB(dbname);
        if(db.authenticate(username, password.toCharArray())) {
            return db;
        } else {
            throw new RuntimeException("Not able to authenticate with MongoDB");
        }

    }
}
