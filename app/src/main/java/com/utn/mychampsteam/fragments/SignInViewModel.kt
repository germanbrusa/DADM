package com.utn.mychampsteam.fragments

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.mychampsteam.entities.User

class SignInViewModel : ViewModel() {
    lateinit var auth: FirebaseAuth
    var rootRef = FirebaseFirestore.getInstance()
    lateinit var user: User
    val emailKey = "EMAIL"
    val nicknameKey = "NICKNAME"
    val uidKey = "UID"
    val avatar = "AVATAR"
    lateinit var prefs: SharedPreferences
}
