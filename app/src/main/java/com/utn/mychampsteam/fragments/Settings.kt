package com.utn.mychampsteam.fragments

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.utn.mychampsteam.MainActivity

import com.utn.mychampsteam.R

class Settings : Fragment() {

    companion object {
        fun newInstance() = Settings()
    }

    private lateinit var settingsVM: SettingsViewModel
    private lateinit var teamchampionsVM: TeamChampionsViewModel

    lateinit var v: View
    lateinit var spAvatar: Spinner
    lateinit var imgAvatar: ImageView

    var listAvatar = listOf("Custom", "Avengers", "Beast", "Black Widow", "Captain America",
                            "Hawkeye","Hulk", "Mjolnir", "Iron Man", "Magneto", "Rogue",
                            "Shield", "Spider Man", "Thanos", "Thor", "Venom", "Wolverine")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.settings_fragment, container, false)
        setHasOptionsMenu(true)

        spAvatar = v.findViewById(R.id.sp_avatar)
        imgAvatar = v.findViewById(R.id.img_avatar)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsVM = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
        teamchampionsVM = ViewModelProvider(requireActivity()).get(TeamChampionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        teamchampionsVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        SetIconAvatar()
        PopulateSpinner(spAvatar, listAvatar, requireContext())

        spAvatar.setSelection(0, false) //Avoid first false entry
        spAvatar.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                teamchampionsVM.prefs.edit().putString(teamchampionsVM.avatar, listAvatar[position]).apply()
                SetIconAvatar()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun PopulateSpinner(spinner: Spinner, list: List<String>, context: Context) {
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)

        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner.setAdapter(arrayAdapter)
    }

    private fun SetIconAvatar() {
        val user_avatar = teamchampionsVM.prefs.getString(teamchampionsVM.avatar, null)
        var icon: Int = 0

        when(user_avatar!!.toLowerCase()) {
            "custom" -> icon = R.drawable.user_48
            "avengers" -> icon = R.drawable.avengers_48
            "beast" -> icon = R.drawable.beast_48
            "black widow" -> icon = R.drawable.black_widow_48
            "captain america" -> icon = R.drawable.captain_america_48
            "hawkeye" -> icon = R.drawable.hawkeye_48
            "hulk" -> icon = R.drawable.hulk_48
            "mjolnir" -> icon = R.drawable.hummer_48
            "iron man" -> icon = R.drawable.iron_man_48
            "magneto" -> icon = R.drawable.magneto_48
            "rogue" -> icon = R.drawable.rogue_48
            "shield" -> icon = R.drawable.shield_40
            "spider man" -> icon = R.drawable.spider_man_48
            "thanos" -> icon = R.drawable.thanos_48
            "thor" -> icon = R.drawable.thor_48
            "venom" -> icon = R.drawable.venom_48
            "wolverine" -> icon = R.drawable.wolverine_48
        }
        imgAvatar.setImageResource(icon)
    }
}
