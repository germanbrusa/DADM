package com.utn.mychampsteam.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.mychampsteam.entities.Champion

class FeatureDataViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var chosenChamp: String? = null
    var myChamp: Champion = Champion()
    var allChamp: Champion = Champion()
    var rootRef = FirebaseFirestore.getInstance()
    val uidKey = "UID"
    lateinit var prefs: SharedPreferences
}
