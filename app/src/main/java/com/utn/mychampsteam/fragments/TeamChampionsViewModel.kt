package com.utn.mychampsteam.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.mychampsteam.entities.Champion

class TeamChampionsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var Champions: MutableList<Champion> = ArrayList<Champion>()
    var rootRef = FirebaseFirestore.getInstance()
    val uidKey = "UID"
    val avatar = "AVATAR"
    lateinit var prefs: SharedPreferences

}
