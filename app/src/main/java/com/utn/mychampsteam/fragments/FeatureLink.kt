//package com.utn.mychampsteam.fragments
//
//import androidx.lifecycle.ViewModelProviders
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.webkit.WebView
//import android.widget.MediaController
//import android.widget.ProgressBar
//import android.widget.VideoView
//import androidx.lifecycle.ViewModelProvider
//
//import com.utn.mychampsteam.R
//import kotlinx.android.synthetic.main.feature_link_fragment.*
//import kotlinx.coroutines.*
//
//class FeatureLink : Fragment() {
//
//    companion object {
//        fun newInstance() = FeatureLink()
//    }
//
//    private lateinit var featurelinkVM: FeatureLinkViewModel
//    private lateinit var featuredataVM: FeatureDataViewModel
//
//    lateinit var v: View
//    lateinit var videoSpecialMoves: VideoView
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        v = inflater.inflate(R.layout.feature_link_fragment, container, false)
//
//        videoSpecialMoves = v.findViewById(R.id.video_special_moves)
//
//        return v
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        val parentJob = Job()
//        val scope = CoroutineScope(Dispatchers.IO + parentJob)
//
//        var mediaController = MediaController(requireContext())
//        videoSpecialMoves.setVideoPath(featuredataVM.myChamp!!.urlVideo)
//        videoSpecialMoves.setMediaController(mediaController)
//        mediaController.setAnchorView(videoSpecialMoves)
//
//        scope.launch {
//            var resultado = async{setProgressBar()}
////            if(resultado.await()){
////                requireActivity().runOnUiThread(Runnable {
////                    setToolBarDatos()
////                })
////            }
//        }
//
//        videoSpecialMoves.setOnPreparedListener {
//            featurelinkVM.cargaCompleta = true
//            videoSpecialMoves.start()
//        }
//    }
//
//    suspend fun setProgressBar() {
//        var progressBar: ProgressBar = v.findViewById(R.id.pbar_loading)
//        while(!featurelinkVM.cargaCompleta) {
//
//        }
//        progressBar.visibility = View.INVISIBLE
//
//    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        featurelinkVM = ViewModelProvider(requireActivity()).get(FeatureLinkViewModel::class.java)
//        featuredataVM = ViewModelProvider(requireActivity()).get(FeatureDataViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
//}

package com.utn.mychampsteam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider

import com.utn.mychampsteam.R
import kotlinx.coroutines.*
import java.lang.Runnable

class FeatureLink : Fragment() {

    companion object {
        fun newInstance() = FeatureLink()
    }

    private lateinit var featurelinkVM: FeatureLinkViewModel
    private lateinit var featuredataVM: FeatureDataViewModel

    lateinit var v: View
    lateinit var videoSpecialMoves: VideoView
    lateinit var pBarLoading: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.feature_link_fragment, container, false)

        videoSpecialMoves = v.findViewById(R.id.video_special_moves)

        return v
    }

    override fun onStart() {
        super.onStart()

        ///****Coroutines ******************************************************************
        val parentJob = Job()

        val scope = CoroutineScope(Dispatchers.IO + parentJob )
        ///*********************************** CORUTINAS ***********************************

        var mediaController = MediaController(requireContext())
        videoSpecialMoves.setVideoPath(featuredataVM.myChamp!!.urlVideo)

        videoSpecialMoves.setMediaController(mediaController)
        mediaController.setAnchorView(videoSpecialMoves)

        scope.launch {
            var resultado = async{setProgressBar()}
            if(resultado.await()) {
                hideProgressBar()
            }
        }

        videoSpecialMoves.setOnPreparedListener{
            featurelinkVM.CargaCompleta = true

            videoSpecialMoves.start()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        featurelinkVM = ViewModelProvider(requireActivity()).get(FeatureLinkViewModel::class.java)
        featuredataVM = ViewModelProvider(requireActivity()).get(FeatureDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

    ///****Coroutines ******************************************************************
    suspend fun setProgressBar (): Boolean{
        pBarLoading = v.findViewById(R.id.pbar_loading)
        while(!featurelinkVM.CargaCompleta){
        }
        return true
    }

    suspend fun hideProgressBar(){
        pBarLoading.visibility = View.INVISIBLE
    }
    ///*********************************************************************************
}