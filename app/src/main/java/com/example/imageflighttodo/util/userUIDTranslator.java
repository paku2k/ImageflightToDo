package com.example.imageflighttodo.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userUIDTranslator {


        // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
        private static userUIDTranslator instance;
        private FirebaseFirestore db;
        private String name;
        private CollectionReference userRef;
        // Verhindere die Erzeugung des Objektes über andere Methoden
        private userUIDTranslator () {
            db=FirebaseFirestore.getInstance();
            userRef=db.collection("users");

        }
        public String translateUID(String uid) {



            return "";





        }
        // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes
        // Objekt erzeugt und dieses zurückliefert.
        public static userUIDTranslator getInstance () {
            if (userUIDTranslator.instance == null) {
                userUIDTranslator.instance = new userUIDTranslator ();
            }
            return userUIDTranslator.instance;
        }

}
