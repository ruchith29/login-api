package com.nextrow.login_api.utilities;

import com.nextrow.login_api.configuration.Constants;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MongoUtilities {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void registerUser(Document userdata){
        mongoTemplate.getDb().getCollection(Constants.COLLECTION_NAME).insertOne(userdata);
    }

    public Document getUser(Query query){
        return mongoTemplate.findOne(query, Document.class, Constants.COLLECTION_NAME);
    }
}
