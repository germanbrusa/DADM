package com.utn.mychampsteam.fragments

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.common.Feature
import com.google.firebase.firestore.DocumentReference
import com.utn.mychampsteam.ChampionFeaturesActivity
import com.utn.mychampsteam.MainActivity

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.Champion
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.SelectClause1
import org.w3c.dom.Text

class FeatureData : Fragment() {

    companion object {
        fun newInstance() = FeatureData()
    }

    private lateinit var featuredataVM: FeatureDataViewModel

    lateinit var v: View
    lateinit var imgPortraitData: ImageView
    lateinit var txtTypeData: TextView
    lateinit var imgTypeData: ImageView
    lateinit var txtTeamStarsData: TextView
    lateinit var imgTeamStarsData: ImageView
    lateinit var txtAllStarsData: TextView
    lateinit var imgAllStarsData: ImageView
    lateinit var txtTeamPowerData: TextView
    lateinit var txtAllPowerData: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.feature_data_fragment, container, false)

        imgPortraitData = v.findViewById(R.id.img_portrait_data)
        txtTypeData = v.findViewById(R.id.txt_type_data)
        imgTypeData = v.findViewById(R.id.img_type_data)
        txtTeamStarsData = v.findViewById(R.id.txt_team_stars_data)
        imgTeamStarsData = v.findViewById(R.id.img_team_stars_Data)
        txtAllStarsData = v.findViewById(R.id.txt_all_stars_data)
        imgAllStarsData = v.findViewById(R.id.img_all_stars_data)
        txtTeamPowerData = v.findViewById(R.id.txt_team_power_data)
        txtAllPowerData = v.findViewById(R.id.txt_all_power_data)

        // Inflate the layout for this fragment
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        featuredataVM = ViewModelProvider(requireActivity()).get(FeatureDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        featuredataVM.chosenChamp = activity?.intent?.getStringExtra("chosenChamp")

        (activity as AppCompatActivity).supportActionBar?.title =
            featuredataVM.chosenChamp.toString()

        fillData()
    }

    fun fillData() {

        featuredataVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userid = featuredataVM.prefs.getString(featuredataVM.uidKey, null)

        val myChampRef = featuredataVM.rootRef.collection("users").document(userid!!)
            .collection("myTeam").document(featuredataVM.chosenChamp.toString())

        val allChampRef = featuredataVM.rootRef.collection("champions").document(featuredataVM.chosenChamp.toString().toLowerCase())

        myChampRef.get().addOnSuccessListener { documentSnapshot ->
            featuredataVM.myChamp = documentSnapshot.toObject(Champion::class.java)!!
        }
            .addOnCompleteListener {
                SetImagePortrait(featuredataVM.myChamp.urlImgPortrait)
                txtTypeData.text = featuredataVM.myChamp.type
                SetImageType(featuredataVM.myChamp.type)
                txtTeamStarsData.text = featuredataVM.myChamp.stars
                SetImageStars(featuredataVM.myChamp.singularAbility, imgTeamStarsData)
                txtTeamPowerData.text = featuredataVM.myChamp.power
            }

        allChampRef.get().addOnSuccessListener { documentSnapshot ->
            featuredataVM.allChamp = documentSnapshot.toObject(Champion::class.java)!!
        }
            .addOnCompleteListener{
                txtAllStarsData.text = featuredataVM.allChamp.stars
                SetImageStars(featuredataVM.allChamp.singularAbility, imgAllStarsData)
                txtAllPowerData.text = featuredataVM.allChamp.power
            }
    }

    fun SetImagePortrait(image: String) {
        Glide.with(this)
            .load(image)
            .into(imgPortraitData)
    }

    fun SetImageType(type: String) {
        when(type) {
            "Cosmic" -> imgTypeData.setImageResource(R.drawable.cosmic)
            "Mutant" -> imgTypeData.setImageResource(R.drawable.mutant)
            "Mystic" -> imgTypeData.setImageResource(R.drawable.mystic)
            "Science" -> imgTypeData.setImageResource(R.drawable.science)
            "Skill" -> imgTypeData.setImageResource(R.drawable.skill)
            "Tech" -> imgTypeData.setImageResource(R.drawable.tech)
        }
    }

    fun SetImageStars(ability: String, view: ImageView) {
        when(ability) {
            "yes" -> view.setImageResource(R.drawable.silver_star_50)
            "no" -> view.setImageResource(R.drawable.gold_star_50)
        }
    }
}
