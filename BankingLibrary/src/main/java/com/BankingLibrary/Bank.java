package com.BankingLibrary;

import static com.mongodb.client.model.Filters.eq;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import org.bson.conversions.Bson;

public class Bank {

    private static final String connectionString = "mongodb+srv://testUser:testPass@cards.ckq39bj.mongodb.net/?retryWrites=true&w=majority";

    public static void addCard(String cardNumber, String cvv, String expirationDate) throws MongoException, BankingExistingCardException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        Document newDocument = new Document();
        newDocument.append("cardNumber", cardNumber);
        newDocument.append("cvv", cvv);
        newDocument.append("expirationDate", expirationDate);
        newDocument.append("sold", 0);

        try {
            getCard(cardNumber, cvv, expirationDate);     //if the card doesn't exist NullPointerException will be trown. Otherwise it exists
            throw new BankingExistingCardException("Card already exists");
        }
        catch (NullPointerException e) {
            //continue because the card doesn't exist already so it can be added
        }
        catch (BankingExistingCardException e) {
            throw e;
        }

        collection.insertOne(newDocument);         //MongoException can come from here
    }

    public static void addCard(String cardNumber, String cvv, String expirationDate, float sold) throws MongoException, BankingExistingCardException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        Document newDocument = new Document();
        newDocument.append("cardNumber", cardNumber);
        newDocument.append("cvv", cvv);
        newDocument.append("expirationDate", expirationDate);
        newDocument.append("sold", sold);

        try {
            getCard(cardNumber, cvv, expirationDate);     //if the card doesn't exist NullPointerException will be trown. Otherwise it exists
            throw new BankingExistingCardException("Card already exists");
        }
        catch (NullPointerException e) {
            //continue because the card doesn't exist already so it can be added
        }
        catch (BankingExistingCardException e) {
            throw e;
        }

        collection.insertOne(newDocument);
    }

    public static Card getCard(String cardNumber, String cvv, String expirationDate) throws NullPointerException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        BasicDBObject searchCriteria = new BasicDBObject();
        searchCriteria.append("cardNumber", cardNumber);
        searchCriteria.append("cvv", cvv);
        searchCriteria.append("expirationDate", expirationDate);

        Document document = collection.find(searchCriteria).first();

        JsonObject jsonObject = JsonParser.parseString(document.toJson()).getAsJsonObject();   //document.toJson() could throw NullPointerException if no card is found
        float sold = jsonObject.get("sold").getAsFloat();

        Card card = new Card(cardNumber, cvv, expirationDate, sold);
        return card;
    }

    public static float getSold(String cardNumber, String cvv, String expirationDate) throws NullPointerException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        BasicDBObject searchCriteria = new BasicDBObject();
        searchCriteria.append("cardNumber", cardNumber);
        searchCriteria.append("cvv", cvv);
        searchCriteria.append("expirationDate", expirationDate);

        Document document = collection.find(searchCriteria).first();

        JsonObject jsonObject = JsonParser.parseString(document.toJson()).getAsJsonObject();   //document.toJson() could throw NullPointerException if no card is found
        float sold = jsonObject.get("sold").getAsFloat();

        return sold;
    }

    public static void setSold(String cardNumber, String cvv, String expirationDate, float newSold) throws MongoException, NullPointerException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        BasicDBObject searchCriteria = new BasicDBObject();
        searchCriteria.append("cardNumber", cardNumber);
        searchCriteria.append("cvv", cvv);
        searchCriteria.append("expirationDate", expirationDate);

        Document document = collection.find(searchCriteria).first();
        Bson updates = Updates.combine(Updates.set("sold", newSold));
        UpdateOptions options = new UpdateOptions().upsert(true);
        collection.updateOne(document, updates, options);     //could throw MongoException
    }

    public static void addSold(String cardNumber, String cvv, String expirationDate, float amount) throws MongoException, NullPointerException {

        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("CardsDB");
        MongoCollection<Document> collection = database.getCollection("Cards");

        BasicDBObject searchCriteria = new BasicDBObject();
        searchCriteria.append("cardNumber", cardNumber);
        searchCriteria.append("cvv", cvv);
        searchCriteria.append("expirationDate", expirationDate);

        Document document = collection.find(searchCriteria).first();

        JsonObject jsonObject = JsonParser.parseString(document.toJson()).getAsJsonObject();   //document.toJson() could throw NullPointerException if no card is found
        float sold = jsonObject.get("sold").getAsFloat();
        sold += amount;

        Bson updates = Updates.combine(Updates.set("sold", sold));
        UpdateOptions options = new UpdateOptions().upsert(true);
        collection.updateOne(document, updates, options);  //could throw MongoException

    }

    public static void reduceSold(String cardNumber, String cvv, String expirationDate, float amount) throws MongoException, NullPointerException {
        addSold(cardNumber, cvv, expirationDate, -amount);  //throws exceptions
    }
}
