package dz.esi.immob.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import dz.esi.immob.R
import dz.esi.immob.api.annonce.Annonce
import dz.esi.immob.databinding.AnnonceBinding
import kotlinx.android.synthetic.main.annonce.view.*

class AnnoncesAdapter(var annonces: List<Annonce>?) : RecyclerView.Adapter<AnnoncesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding  = DataBindingUtil.inflate<AnnonceBinding>(LayoutInflater.from(parent.context), R.layout.annonce, parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount() = annonces?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            annonce = annonces?.get(position)
            root.annonceImageView
            val circularProgressDrawable = CircularProgressDrawable(root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(root)
                .load(annonce?.image)
                .fitCenter()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.no_image_available)
                .into(root.annonceImageView)
        }
    }

    class MyViewHolder(val binding: AnnonceBinding): RecyclerView.ViewHolder(binding.root)
}