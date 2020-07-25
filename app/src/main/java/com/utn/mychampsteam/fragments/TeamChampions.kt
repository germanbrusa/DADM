package com.utn.mychampsteam.fragments

import android.app.AlertDialog
import android.app.AppComponentFactory
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.telephony.mbms.MbmsErrors
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.utn.mychampsteam.ChampionFeaturesActivity
import com.utn.mychampsteam.MainActivity

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.Champion
import com.utn.mychampsteam.holders.ChampionHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class TeamChampions : Fragment() {

    companion object {
        fun newInstance() = TeamChampions()
    }

    private lateinit var teamchampionsVM: TeamChampionsViewModel

    lateinit var v: View
    lateinit var recTeamChampions: RecyclerView
    lateinit var btnAdd: FloatingActionButton

    private lateinit var adapter: FirestoreRecyclerAdapter<Champion, ChampionHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.team_champions_fragment, container, false)
        setHasOptionsMenu(true)
        //Recycler
        btnAdd = v.findViewById(R.id.btn_add)

        recTeamChampions = v.findViewById(R.id.rec_team_champions)
        recTeamChampions.setHasFixedSize(true)
        recTeamChampions.layoutManager = LinearLayoutManager(context)

        // Inflate the layout for this fragment
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        teamchampionsVM =
            ViewModelProvider(requireActivity()).get(TeamChampionsViewModel::class.java)
//        val avatar_icon = teamchampionsVM.prefs.getString(teamchampionsVM.avatar, null)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
//        (activity as AppCompatActivity).supportActionBar?.setIcon(R.drawable.user_48)

        fillRecycler()
    }

    fun fillRecycler() {
        teamchampionsVM.prefs = requireContext().getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        val userid = teamchampionsVM.prefs.getString(teamchampionsVM.uidKey, null)
//        val rootRef = FirebaseFirestore.getInstance()
        val myTeamRef = teamchampionsVM.rootRef.collection("users").document(userid!!)
            .collection("myTeam")

        val options = FirestoreRecyclerOptions.Builder<Champion>()
            .setQuery(myTeamRef, Champion::class.java)
            .build()


        adapter = object : FirestoreRecyclerAdapter<Champion, ChampionHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_champion, parent, false)
                return ChampionHolder(view)
            }

            override fun onBindViewHolder(holder: ChampionHolder, position: Int, model: Champion) {
                holder.setName(model.name)
                holder.setImagePortrait(model.urlImgPortrait)
                holder.getCardLayout().setOnClickListener {
                    ShowFeatures(model.name)
                }
                holder.getCardLayout().setOnLongClickListener {
                    Snackbar.make(v, "LongClick", Snackbar.LENGTH_SHORT).show()
                    if (teamchampionsVM.Champions.isEmpty()) {
                        teamchampionsVM.Champions.add(model)
                        holder.setTextColorSelected()
                    }
                    else {
                        if (teamchampionsVM.Champions.contains(model)) {
                            teamchampionsVM.Champions.remove(model)
                            holder.setTextColorUnselected()
                        }
                        else {
                            teamchampionsVM.Champions.add(model)
                            holder.setTextColorSelected()
                        }
                    }
                    return@setOnLongClickListener true
                }
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
        adapter.startListening()
        recTeamChampions.adapter = adapter
    }

    private fun ShowFeatures(champName: String) {
        val intent = Intent(activity, ChampionFeaturesActivity::class.java)
        intent.putExtra("chosenChamp", champName)
        activity?.startActivity(intent)
        activity?.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Any? = when (item.itemId) {
            R.id.action_edit -> {
                if (teamchampionsVM.Champions.isNotEmpty()) {
                    if (teamchampionsVM.Champions.size == 1) {
                        var champToEdit = teamchampionsVM.Champions[0]
                        EditChampionDialog(champToEdit)
                    } else {                      /*teamchampionsVM.Champions.size > 1*/
                        Snackbar.make(v, "Select a single champion to edit", Snackbar.LENGTH_SHORT)
                            .show()
                        teamchampionsVM.Champions.clear()
                    }
                } else {                          /*teamchampionsVM.Champions.isEmpty()*/
                    Snackbar.make(v, "Choose a champion to edit", Snackbar.LENGTH_SHORT).show()
                }
            }
            R.id.action_delete -> {
                if (teamchampionsVM.Champions.isNotEmpty()) {
                    var champsToErase = teamchampionsVM.Champions
                    EraseChampionDialog(champsToErase)
                } else {
                    Snackbar.make(
                        v,
                        "Select one or more champions to delete",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }

    fun EditChampionDialog(champ: Champion) {
        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        var dialogLayout = inflater.inflate(R.layout.edit_team_champions_dialog, null)
        var txtChampName = dialogLayout.findViewById<TextView>(R.id.txt_champ_name_dialog)
        var edtChampStars = dialogLayout.findViewById<EditText>(R.id.edt_champ_stars_dialog)
        var edtChampSingularAbility =
            dialogLayout.findViewById<EditText>(R.id.edt_champ_singular_ability_dialog)
        var edtChampPower = dialogLayout.findViewById<EditText>(R.id.edt_champ_power_dialog)

        builder.setTitle("Edit Champion")
        builder.setView(dialogLayout)

        txtChampName.text = champ.name
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (edtChampStars.text.isNotEmpty() && edtChampSingularAbility.text.isNotEmpty() && edtChampPower.text.isNotEmpty()) {
                            champ.stars = edtChampStars!!.text.toString()
                            champ.singularAbility = edtChampSingularAbility!!.text.toString()
                            champ.power = edtChampPower!!.text.toString()
                            teamchampionsVM.rootRef
                                .collection("users").document(champ.userID)
                                .collection("myTeam").document(champ.name)
                                .set(champ)
                        } else
                            Snackbar.make(v, "Complete all the fields", Snackbar.LENGTH_SHORT)
                                .show()
                    }
                }
            }
        builder.setPositiveButton("Done", dialogClickListener)
        builder.setNeutralButton("Cancel", null)
        builder.show()
    }

    private fun EraseChampionDialog(champs: MutableList<Champion>) {
        val builder = AlertDialog.Builder(this.context)

        if (champs.isNotEmpty()) {
            if (champs.size == 1)
            {
                builder.setTitle("Delete Champion")
                builder.setMessage("You are about to delete ${champs.size} champion, are you sure?")
            }
            else
            {
                builder.setTitle("Delete Champions")
                builder.setMessage("You are about to delete ${champs.size} champions, are you sure?")
            }
            builder.setPositiveButton("Accept", null)
        }

        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (champs.isNotEmpty()) {
                            for (chosenchamp in champs) {
                                teamchampionsVM.rootRef
                                    .collection("users").document(chosenchamp.userID)
                                    .collection("myTeam").document(chosenchamp.name)
                                    .delete()
                            }
                        }
                        champs.clear()
                    }
                }
            }
        builder.setPositiveButton("OK!", dialogClickListener)
        builder.setNeutralButton("Cancel", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}