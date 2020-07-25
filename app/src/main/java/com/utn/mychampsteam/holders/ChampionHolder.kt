package com.utn.mychampsteam.holders

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors.getColor
import com.utn.mychampsteam.R
import com.utn.mychampsteam.R.*
import kotlinx.android.synthetic.main.item_champion.view.*
import kotlin.coroutines.coroutineContext

class ChampionHolder (v: View) : RecyclerView.ViewHolder(v) {

    private var view: View

    init {
        this.view = v
    }

    fun setTextColorSelected() {
        view.txt_name_item.setTextColor(getColor(view.context, color.colorAccent))
    }

    fun setTextColorUnselected() {
        view.txt_name_item.setTextColor(getColor(view.context, color.colorPrimary))
    }

    fun clearSelections() {

    }

    fun setName(name: String){
        val txt: TextView = view.findViewById(id.txt_name_item)
        txt.text = name
    }

    fun getCardLayout(): CardView {
        return view.findViewById(id.card_champ_item)
    }

    fun setImagePortrait (imgPortrait: String){
        val image: ImageView = view.findViewById(id.img_portrait_item)
        Glide.with(view.card_champ_item)
            .load(imgPortrait)
            .into(image)
    }
}