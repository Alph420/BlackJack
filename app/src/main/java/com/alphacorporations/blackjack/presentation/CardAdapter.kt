package com.alphacorporations.blackjack.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphacorporations.blackjack.R
import com.alphacorporations.blackjack.data.Card
import com.alphacorporations.blackjack.databinding.CardItemBinding
import com.alphacorporations.blackjack.utils.Utils

/**
 * Created by Julien Jennequin on 07/02/2024 12:11
 * Project : BlackJack
 **/
class CardAdapter : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var dataList: MutableList<Card> = mutableListOf()


    class CardViewHolder(val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val binding = holder.binding
        val card = dataList[position]


        if (card.isVisible) {
            binding.card.setImageDrawable(
                Utils.getCardImage(
                    card.name,
                    binding.root.context
                )
            )
        } else {
            binding.card.setImageDrawable(
                binding.root.context.getDrawable(R.drawable.back)
            )
        }

    }

    fun setDataList(equipment: List<Card>) {
        this.clear()
        this.dataList.addAll(equipment)
        notifyDataSetChanged()
    }

    fun isNotEmpty(): Boolean {
        return dataList.isNotEmpty()
    }

    fun removeItem(position: Int) {
        this.dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }
}