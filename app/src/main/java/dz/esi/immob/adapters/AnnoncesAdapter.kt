package dz.esi.immob.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dz.esi.immob.R
import dz.esi.immob.api.annonce.Annonce
import dz.esi.immob.databinding.AnnonceBinding

class AnnoncesAdapter(val annonces: List<Annonce>) : RecyclerView.Adapter<AnnoncesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding  = DataBindingUtil.inflate<AnnonceBinding>(LayoutInflater.from(parent.context), R.layout.annonce, parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount() = annonces.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.annonce = annonces[position]
    }

    class MyViewHolder(val binding: AnnonceBinding): RecyclerView.ViewHolder(binding.root)
}