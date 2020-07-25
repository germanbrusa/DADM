package com.utn.mychampsteam.fragments

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

import com.utn.mychampsteam.R
import com.utn.mychampsteam.entities.Champion
import com.utn.mychampsteam.holders.ChampionHolder
import kotlinx.android.synthetic.main.item_champion.*
import kotlinx.android.synthetic.main.team_champions_fragment.*
import kotlin.properties.Delegates

class AllChampions : Fragment() {

    companion object {
        fun newInstance() = AllChampions()
    }

    private lateinit var allchampionsVM: AllChampionsViewModel
    private lateinit var teamchampionsVM: TeamChampionsViewModel

    lateinit var v: View
    lateinit var recAllChampions: RecyclerView
    lateinit var btnDone: FloatingActionButton

    private lateinit var adapter: FirestoreRecyclerAdapter<Champion, ChampionHolder>

    private var test: Boolean = false
//    val storage = Firebase.storage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.all_champions_fragment, container, false)
        //Recycler
        btnDone = v.findViewById(R.id.btn_done)

        recAllChampions = v.findViewById(R.id.rec_all_champions)
        recAllChampions.setHasFixedSize(true)
        recAllChampions.layoutManager = LinearLayoutManager(context)

        // Inflate the layout for this fragment
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        allchampionsVM = ViewModelProvider(requireActivity()).get(AllChampionsViewModel::class.java)
        teamchampionsVM = ViewModelProvider(requireActivity()).get(TeamChampionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

//        teamchampionsVM.prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userid = teamchampionsVM.prefs.getString(teamchampionsVM.uidKey, null)
        fillRecycler()
         btnDone.setOnClickListener {
             if(allchampionsVM.Champions.isNotEmpty()) {
                for(champ in allchampionsVM.Champions) {
                    champ.userID = userid.toString()
                    teamchampionsVM.rootRef
                        .collection("users").document(userid!!)
                        .collection("myTeam").document(champ.name)
                        .set(champ)
                }
//                 allchampionsVM.Champions.clear()
//                 test = true
             }
         }
    }

    fun fillRecycler(){
//        val rootRef = FirebaseFirestore.getInstance()
        val championsRef = teamchampionsVM.rootRef.collection("champions")

        val options = FirestoreRecyclerOptions.Builder<Champion>()
            .setQuery(championsRef, Champion::class.java)
            .build()

        adapter = object: FirestoreRecyclerAdapter<Champion, ChampionHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChampionHolder {
               val view = LayoutInflater.from(parent.context)
                   .inflate(R.layout.item_champion, parent, false)
                return ChampionHolder(view)
            }

            override fun onBindViewHolder(holder: ChampionHolder, position: Int, model: Champion) {
                holder.setName(model.name)
                holder.setImagePortrait(model.urlImgPortrait)
                holder.getCardLayout().setOnClickListener {
                    Snackbar.make(v, "ShortClick", Snackbar.LENGTH_SHORT).show()
                }
                holder.getCardLayout().setOnLongClickListener {
                    Snackbar.make(v, "LongClick", Snackbar.LENGTH_SHORT).show()
                    if(allchampionsVM.Champions.isEmpty() ){
                        allchampionsVM.Champions.add(model)
                        holder.setTextColorSelected()
                    }
                    else {
                        if(allchampionsVM.Champions.contains(model)) {
                            allchampionsVM.Champions.remove(model)
                            holder.setTextColorUnselected()
                        }
                        else {
                            allchampionsVM.Champions.add(model)
                            holder.setTextColorSelected()
                        }
                    }
                    return@setOnLongClickListener true
                }
//                if(test)
//                {
//                    for(champs in allchampionsVM.Champions) {
//                            holder.setTextColorUnselected()
//                            notifyDataSetChanged()
//                        }
//                    }
//                    test=false
//                }
            }
            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
        adapter.startListening()
        recAllChampions.adapter = adapter
    }

}