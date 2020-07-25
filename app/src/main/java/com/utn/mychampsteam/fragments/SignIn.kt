package com.utn.mychampsteam.fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.DescriptorProtos
import com.utn.mychampsteam.MainActivity

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.User

class SignIn : Fragment() {

    companion object {
        fun newInstance() = SignIn()
    }

    private lateinit var signinVM: SignInViewModel
    private lateinit var loginVM: LoginViewModel

    lateinit var v: View
    lateinit var edtMailSignIn: EditText
    lateinit var edtPassSignIn: EditText
    lateinit var edtNickname: EditText
    lateinit var btnSignIn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.sign_in_fragment, container, false)

        edtMailSignIn = v.findViewById(R.id.edt_mail_signin)
        edtPassSignIn = v.findViewById(R.id.edt_pass_signin)
        edtNickname = v.findViewById(R.id.edt_nick_name)
        btnSignIn = v.findViewById(R.id.btn_sign_in)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signinVM = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)
        loginVM = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        signinVM.auth = Firebase.auth
        signinVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        signin()
    }

    private fun signin() {
        btnSignIn.setOnClickListener {
            if (edtMailSignIn.text.isNotEmpty() && edtPassSignIn.text.isNotEmpty()) {
                signinVM.auth.createUserWithEmailAndPassword(edtMailSignIn.text.toString(),
                    edtPassSignIn.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var actualUser = User()
                        actualUser.uid = signinVM.auth.currentUser!!. uid
                        actualUser.email = signinVM.auth.currentUser!!.email.toString()
                        actualUser.nickname = edtNickname!!.text.toString()
                        actualUser.avatar = "Custom"
//                        signinVM.user.uid = signinVM.auth.currentUser!!.uid
//                        signinVM.user.email = signinVM.auth.currentUser!!.email.toString()
//                        signinVM.user.nickname = edtNickname!!.text.toString()
                        val editor = signinVM.prefs.edit()
                        editor.putString(signinVM.emailKey, actualUser.email)
                        editor.putString(signinVM.nicknameKey, actualUser.nickname)
                        editor.putString(signinVM.uidKey, actualUser.uid)
                        editor.putString(signinVM.avatar, actualUser.avatar)
                        editor.apply()
//                        CreateUser(signinVM.auth.currentUser!!.uid, signinVM.auth.currentUser!!.email.toString(), edtNickname!!.text.toString())
//                        CreateUser(signinVM.user.uid, signinVM.user.email, signinVM.user.nickname)
                        CreateUser(actualUser.uid, actualUser.email, actualUser.nickname, actualUser.avatar)
                        ShowMain()
                    } else {
                        var authError = it.exception?.message.toString()
                        ShowAlert(authError)
                    }
                }

            }
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

    private fun CreateUser(uid: String, mail: String, nick: String, avatar: String) {
//        val rootRef = FirebaseFirestore.getInstance()
        signinVM.user = User(uid, mail, nick, avatar)
        signinVM.rootRef.collection("users").document(signinVM.user.uid)
            .set(signinVM.user)
            .addOnSuccessListener {
//                Snackbar.make(v, "Usuario creado", Snackbar.LENGTH_SHORT ).show()
            }
            .addOnFailureListener {
//                Snackbar.make(v, "Usuario no creado", Snackbar.LENGTH_SHORT ).show()
            }
    }
}
