package com.foundernest.screener.domain.investment.infrastructure;

import com.foundernest.screener.domain.investment.core.model.Company;
import com.foundernest.screener.domain.investment.core.model.Criteria;
import com.foundernest.screener.domain.investment.core.model.User;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CompanyOutgoing;
import com.foundernest.screener.domain.investment.core.ports.outgoing.CriteriaOutgoing;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class InvestmentAdapter implements CriteriaOutgoing, CompanyOutgoing {
    Logger logger = LoggerFactory.getLogger(InvestmentAdapter.class);
    public static final String COLLECTION_NAME = "users";

    @Override
    public Criteria addUserCriteriaHave(String user, Criteria criteria) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(user);
        Criteria finalCriteria = null;
        if (!criteria.getMustHave().isEmpty()) {
            for (String mustHave : criteria.getMustHave()) {
                documentReference.update("criteria.mustHave", FieldValue.arrayUnion(mustHave));
            }
        }
        if (!criteria.getSuperNiceToHave().isEmpty()) {
            for (String superNiceToHave : criteria.getSuperNiceToHave()) {
                documentReference.update("criteria.superNiceToHave",
                        FieldValue.arrayUnion(superNiceToHave));
            }
        }
        if (!criteria.getNiceToHave().isEmpty()) {
            for (String niceToHave : criteria.getNiceToHave()) {
                documentReference.update("criteria.niceToHave",
                        FieldValue.arrayUnion(niceToHave));
            }
        }
        try {
            finalCriteria = documentReference.get().get().toObject(Criteria.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
        return finalCriteria;
    }

    @Override
    public Criteria deleteUserCriteriaHave(String user, Criteria criteria) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(user);
        Criteria finalCriteria = null;
        if (!criteria.getMustHave().isEmpty()) {
            for (String mustHave : criteria.getMustHave()) {
                documentReference.update("criteria.mustHave", FieldValue.arrayRemove(mustHave));
            }
        }
        if (!criteria.getSuperNiceToHave().isEmpty()) {
            for (String superNiceToHave : criteria.getSuperNiceToHave()) {
                documentReference.update("criteria.superNiceToHave",
                        FieldValue.arrayRemove(superNiceToHave));
            }
        }
        if (!criteria.getNiceToHave().isEmpty()) {
            for (String niceToHave : criteria.getNiceToHave()) {
                documentReference.update("criteria.niceToHave",
                        FieldValue.arrayRemove(niceToHave));
            }
        }
        try {
            finalCriteria = documentReference.get().get().toObject(Criteria.class);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
        return finalCriteria;
    }

    @Override
    public Criteria getUserCriteria(String user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(user);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }

        if (document != null) {
            if (document.exists()) {
                User userObj = document.toObject(User.class);
                if (userObj != null) {
                    return userObj.getCriteria();
                }
            }
        }
        return null;
    }

    @Override
    public List<Company> getAllUserCompanies(String name) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }

        if (document != null) {
            if (document.exists()) {
                User userObj = document.toObject(User.class);
                if (userObj != null) {
                    return userObj.getCompanies();
                }
            }
        }
        return null;
    }

    @Override
    public Company getUserCompanyDetails(String name, String company) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = null;

        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }

        if (document != null) {
            if (document.exists()) {
                User userObj = document.toObject(User.class);
                if (userObj != null) {
                    return userObj.getCompanies().stream().filter((c) -> c.getName().equals(company)).findFirst().orElse(null);
                }
            }
        }
        return null;
    }

    @Override
    public String makeADecision(String name, String company, int decision) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        ApiFuture<WriteResult> collectionsApiFuture;
        DocumentSnapshot document;

        try {
            document = future.get();
            if (document != null) {
                if (document.exists()) {
                    User userObj = document.toObject(User.class);
                    if (userObj != null) {
                        userObj.getCompanies().forEach((c) -> {
                            if (c.getName().equals(company)) {
                                c.setMeet(decision);
                            }
                        });
                        collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).set(userObj);
                        return collectionsApiFuture.get().getUpdateTime().toString();
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String updateCompanyHaves(String name, Company company) {
        return null;
    }
}
