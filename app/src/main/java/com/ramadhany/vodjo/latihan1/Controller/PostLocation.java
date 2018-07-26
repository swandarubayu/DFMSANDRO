package com.ramadhany.vodjo.latihan1.Controller;


import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostLocation {
    private DatabaseReference firebaseDb = FirebaseDatabase.getInstance().getReference();

    public PostLocation(Location location) {
        this.firebaseDb = firebaseDb;
    }
}
