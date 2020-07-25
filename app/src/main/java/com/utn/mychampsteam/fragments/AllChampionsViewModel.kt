package com.utn.mychampsteam.fragments

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.mychampsteam.entities.Champion

class AllChampionsViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var Champions: MutableList<Champion> = ArrayList<Champion>()
    var rootRef = FirebaseFirestore.getInstance()
}
