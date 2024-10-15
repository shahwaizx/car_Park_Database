package Connection;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;


public class mongo_connection {






//    public void insertCustomer(int customerId,String name, String cnic, String phoneNo, String username, String password){
//        try{
//            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
//            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
//            MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
//
//            Document customerDocument = new Document("_id",customerId)
//                    .append("name", name)
//                    .append("cnic", cnic)
//                    .append("phone_no", phoneNo)
//                    .append("username", username)
//                    .append("password", password);
//            collection.insertOne(customerDocument);
//            System.out.println("Customer registered successfully");
//
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }


//        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {
//            MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
//            MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
//            System.out.println("Number of Documents in the 'customer' collection: " + collection.countDocuments());
//
//
//            // Query all documents in the collection
//            FindIterable<Document> documents = collection.find();
//
//            for (Document document : documents) {
//                try {
//                    String name = document.getString("name");
//                    String cnic = document.getString("cnic");
//                    System.out.println("Name: " + name + ", CNIC: " + cnic);
//                    System.out.println("-------------------------------------");
//                } catch (Exception e) {
//                    System.err.println("Error extracting data from document: " + e.getMessage());
//                }
//            }
//        }

public static void main(String[] args) {
    try {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
        MongoCollection<Document> collection = mongoDatabase.getCollection("booking");

        // Define the filter for the delete operation
        Document filter = new Document("idBooking", 0);

        // Perform the delete operation
        collection.deleteOne(filter);

        // Check if any document was deleted
    } catch (Exception e) {
        e.printStackTrace();

    }

//    try{
//        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/");
//        MongoDatabase mongoDatabase = mongoClient.getDatabase("carpark");
//        MongoCollection<Document> collection = mongoDatabase.getCollection("customer");
//
//        Document lastcustomerID = collection.find()
//                .sort(Sorts.descending("customer_id"))
//                .limit(1)
//                .first();
//        int lastCustomerId = lastcustomerID.getInteger("customer_id");
//        System.out.println("last id: " + lastCustomerId);
//
//        int newCustomerId = lastCustomerId + 1;
//        System.out.println("new id: " + newCustomerId);
//        Document customerDocument = new Document("customer_id",newCustomerId)
//                .append("name", "Yasir")
//                .append("cnic", "8230398320175")
//                .append("phone_no", "03198564765")
//                .append("username", "yasirkhan123")
//                .append("password", "khanyasir");
//        collection.insertOne(customerDocument);
//        System.out.println("Customer registered successfully");
//
//    }
//    catch(Exception e){
//        e.printStackTrace();
//    }


}


}

