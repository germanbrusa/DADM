package com.utn.mychampsteam.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.utn.mychampsteam.MainActivity

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.User
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.sign_in_fragment.*

class Login : Fragment() {

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var loginVM: LoginViewModel
    private lateinit var signinVM: SignInViewModel

    lateinit var v: View
    lateinit var edtMailLogin: EditText
    lateinit var edtPassLogin: EditText
    lateinit var btnLogin: Button
    lateinit var clickSignIn: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.login_fragment, container, false)

        edtMailLogin = v.findViewById(R.id.edt_mail_login)
        edtPassLogin = v.findViewById(R.id.edt_pass_login)
        btnLogin = v.findViewById(R.id.btn_login)
        clickSignIn = v.findViewById(R.id.click_sign_in)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginVM = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        signinVM = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        signinVM.auth = Firebase.auth

        login()
        islogged()
    }

    private fun islogged(){
        signinVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = signinVM.prefs.getString(signinVM.emailKey, null)
        val nickname = signinVM.prefs.getString(signinVM.nicknameKey, null)

        if(email != null && nickname != null){
            authLayout.visibility = View.INVISIBLE
            ShowMain()
        }
    }

    private fun login() {
         btnLogin.setOnClickListener {
             if ((edtMailLogin.text.isNotEmpty() && edtPassLogin.text.isNotEmpty())) {
                signinVM.auth.signInWithEmailAndPassword(edtMailLogin.text.toString(),
                    edtPassLogin.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var actualUser = User()
                        val currentUserRef = signinVM.rootRef.collection("users").document(signinVM.auth.currentUser!!.uid)

                        currentUserRef.get().addOnSuccessListener { documentSnapshot ->
                            signinVM.user = documentSnapshot.toObject(User::class.java)!!
                        }
                            .addOnCompleteListener {
                                actualUser.uid = signinVM.user.uid
                                actualUser.email = signinVM.user.email
                                actualUser.nickname = signinVM.user.nickname
                                actualUser.avatar = signinVM.user.avatar
                                signinVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                                val editor = signinVM.prefs.edit()
                                editor.putString(signinVM.emailKey, actualUser.email)
                                editor.putString(signinVM.nicknameKey, actualUser.nickname)
                                editor.putString(signinVM.uidKey, actualUser.uid)
                                editor.putString(signinVM.avatar, actualUser.avatar)
                                editor.apply()
                                ShowMain()
                            }
                    } else {
                        var authError = it.exception?.message.toString()
                        ShowAlert(authError)
                    }
                }
             }
             else if(edtMailLogin.text.isEmpty() || edtPassLogin.text.isEmpty()) {
                 ShowAlert("Ooops, the user cannot be logged. Please complete all the fields and try again")
             }
         }

        clickSignIn.setOnClickListener {
            val loginToSignin = LoginDirections.actionLoginToSignIn()
            v.findNavController().navigate(loginToSignin)

        }
    }

    private fun ShowAlert(error: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Error")
        builder.setMessage(error)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun ShowMain() {
        val intent = Intent(activity, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
}