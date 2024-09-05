package com.ezgiyilmaz.artbooknavigation

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezgiyilmaz.artbooknavigation.databinding.RecyclerRowBinding

class ArtAdapter(val artList:ArrayList<Art>): RecyclerView.Adapter<ArtAdapter.ArtHolder>() {

    class ArtHolder(val binding: RecyclerRowBinding) :RecyclerView.ViewHolder(binding.root){

    }
 // neyi bağlayacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)

    }

    override fun getItemCount(): Int {
        return artList.size
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) { // bağlanınca ne olacak
        holder.binding.recyclerRowTextView.text=artList.get(position).name
        holder.itemView.setOnClickListener { // bu positiondaki item a tıklandığında ne yapsın
            val intent =Intent(holder.itemView.context,DetailFragment::class.java)
            holder.itemView.context.startActivity(intent)
        }
        }
    }