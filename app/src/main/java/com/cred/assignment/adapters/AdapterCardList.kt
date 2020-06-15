package com.cred.assignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cred.assignment.R
import com.cred.assignment.models.CardModel
import kotlinx.android.synthetic.main.fragment_add_card.*
import kotlinx.android.synthetic.main.item_card.view.*

class AdapterCardList(var mContext:Context, var arrList:ArrayList<CardModel>) : RecyclerView.Adapter<AdapterCardList.CardViewHolder>() {


    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvCardNumber = itemView.tvCardNumber
        val tvName = itemView.tvName
        val ivBrandImg = itemView.ivBrandImg
        val parentLayout = itemView.parentLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false))
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.tvCardNumber.text = arrList[position].cardNumber
        holder.tvName.text = arrList[position].name
        when(arrList[position].brand)
        {
            "visa"-> {
                holder.ivBrandImg.setImageResource(R.drawable.visa)
                holder.parentLayout.background = ContextCompat.getDrawable(mContext, R.drawable.card_backg_visa)
            }
            "master"->
            {
                holder.ivBrandImg.setImageResource(R.drawable.master)
                holder.parentLayout.background = ContextCompat.getDrawable(mContext, R.drawable.card_backg)
            }
            "diners"-> {
                holder.ivBrandImg.setImageResource(R.drawable.diners)
                holder.parentLayout.background = ContextCompat.getDrawable(mContext, R.drawable.card_backg_diner)
            }
            "amex"-> {
                holder.ivBrandImg.setImageResource(R.drawable.amex)
                holder.parentLayout.background = ContextCompat.getDrawable(mContext, R.drawable.card_backg_amex)
            }
        }
    }
}