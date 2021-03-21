package com.vasilije.doggo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vasilije.doggo.R
import com.vasilije.doggo.model.Doggo
import com.vasilije.doggo.ui.DetailsActivity


class DogsAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Doggo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DogsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_grid_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is DogsViewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener(View.OnClickListener {

                    val pictureUrl = items[position].pictureUrl
                    val breed = items[position].breed

                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("pictureUrl", pictureUrl)
                    intent.putExtra("breed", breed)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    context.startActivity(intent)
                })
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(dogsList: List<Doggo>) {
        items = dogsList
    }

    class DogsViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val dogImage: ImageView = itemView.findViewById(R.id.dogImageView)
        val breed = itemView.findViewById<TextView>(R.id.breedTextView)
        val shortText = itemView.findViewById<TextView>(R.id.shortText)

        fun bind(doggo: Doggo) {

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(doggo.pictureUrl)
                .into(dogImage)
            breed.text = doggo.breed
            shortText.text = doggo.shortText
        }

    }
}

