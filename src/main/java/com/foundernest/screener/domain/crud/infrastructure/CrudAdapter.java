package com.foundernest.screener.domain.crud.infrastructure;

import com.foundernest.screener.domain.crud.core.model.User;
import com.foundernest.screener.domain.crud.core.ports.outgoing.UserOutgoing;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class CrudAdapter implements UserOutgoing {
    Logger logger = LoggerFactory.getLogger(CrudAdapter.class);
    public static final String COLLECTION_NAME = "users";

    @Override
    public String createUser(User user) {
        ApiFuture<WriteResult> collectionsApiFuture;
        try {
            Firestore dbFireStore = FirestoreClient.getFirestore();
            collectionsApiFuture = dbFireStore.collection(COLLECTION_NAME).document(user.getName()).set(user);
            return collectionsApiFuture.get().getUpdateTime().toString();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserDetails(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = null;
        User user;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }

        if (document != null) {
            if(document.exists()) {
                user = document.toObject(User.class);
                return user;
            }
        }
        return null;
    }

    @Override
    public String updateUserDetails(User user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture =
                dbFirestore.collection(COLLECTION_NAME).document(user.getName()).set(user);
        try {
            return collectionsApiFuture.get().getUpdateTime().toString();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String deleteUser(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return "Document with User name "+ name +" has been deleted";
    }
}
