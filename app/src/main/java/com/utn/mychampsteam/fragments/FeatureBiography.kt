package com.utn.mychampsteam.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.Champion
import java.net.URL
import java.net.URLConnection

class FeatureBiography : Fragment() {

    companion object {
        fun newInstance() = FeatureBiography()
    }

    private lateinit var featurebiographyVM: FeatureBiographyViewModel
    private lateinit var featuredataVM: FeatureDataViewModel

    lateinit var v: View
    lateinit var imgFeaturedBio: ImageView
    lateinit var txtBio: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.feature_biography_fragment, container, false)

        imgFeaturedBio = v.findViewById(R.id.img_featured_bio)
        txtBio = v.findViewById(R.id.txt_bio)

        // Inflate the layout for this fragment
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        featurebiographyVM = ViewModelProvider(requireActivity()).get(FeatureBiographyViewModel::class.java)
        featuredataVM = ViewModelProvider(requireActivity()).get(FeatureDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        fillData()
    }

    private fun fillData() {
        SetImageFeatured(featuredataVM.myChamp.urlImgFeatured)
        txtBio.text = featuredataVM.myChamp.urlBiography
    }

    fun SetImageFeatured(image: String) {
        Glide.with(this)
            .load(image)
            .into(imgFeaturedBio)
    }
}
