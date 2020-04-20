import be.ugent.rml.Executor;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStoreFactory;
import be.ugent.rml.store.RDF4JStore;
import be.ugent.rml.store.QuadStore;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map;

import virtuoso.jena.driver.*;

public class ExecuteMapping {

    public static void main(String[] args){

        String mappingPath = "./mappings/mapping02.ttl";
        String propertiesPath = "./utils/config.properties";

        try {
            File mappingFile = new File(mappingPath);
            InputStream mappingStream = new FileInputStream(mappingFile);
            QuadStore rmlStore = QuadStoreFactory.read(mappingStream);

            // Set up the basepath for the records factory
            RecordsFactory factory = new RecordsFactory("./");
            // Create the executor
            Executor executor = new Executor(rmlStore, factory, null);
            // Execute the mapping
            QuadStore result = executor.execute(null);

            String graph = result.toString().split(" ")[3].split("\n")[0];
            // Review two things: is it possible to create a new graph from the client side?
            // How to incorporate graph information in the mappings to rml can generate it?
            graph = "<http://localhost:8890/kpi>";

            String datasetNTriple = result.toString().replaceAll(graph, " .").replaceAll("null", " .");
            // Get credentials to access to virtuoso sparql service "connection"
            String user = "";
            String pass = "";
            String connection = "";

            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(new File(propertiesPath)));
                user = properties.getProperty("user");
                pass = properties.getProperty("pass");
                connection = properties.getProperty("connection");
            } catch (Exception e){
                e.printStackTrace();
            }

            VirtGraph set = new VirtGraph(connection, user, pass);
            String str = "CLEAR GRAPH " + graph;
            VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str, set);
            vur.exec();

            str = "INSERT INTO GRAPH " + graph + " { " + datasetNTriple + " } ";
            vur = VirtuosoUpdateFactory.create(str, set);
            vur.exec();

        } catch (Exception e) {
            System.out.println("Not done");
            System.out.println(e);
        }

    }

}
