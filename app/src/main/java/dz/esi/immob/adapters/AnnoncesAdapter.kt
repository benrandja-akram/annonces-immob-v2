package dz.esi.immob.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import dz.esi.immob.R
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.databinding.AnnonceBinding
import dz.esi.immob.repositories.User
import dz.esi.immob.repositories.UserData
import kotlinx.android.synthetic.main.annonce.view.*

class AnnoncesAdapter(var annonces: List<Annonce>?, private var listener: OnFavAnnonceChangedListener) :
    RecyclerView.Adapter<AnnoncesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<AnnonceBinding>(
            LayoutInflater.from(parent.context),
            R.layout.annonce,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun getItemCount() = annonces?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.apply {
            annonce = annonces?.get(position).also { annonce ->
                annonce?.favorite = if(UserData.instance.getFavAnnonces().value?.find {it.id == annonce?.id } != null ) 1 else 0
            }

            val circularProgressDrawable = CircularProgressDrawable(root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()


            prefBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { p0, pref, p2 ->
                annonce?.favorite = pref.toInt()
                listener.onFavChanged(annonce?.id!!, pref == 1f)
            }
            Glide.with(root)
                .load(annonce?.image)
                .fitCenter()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.no_image_available)
                .into(root.annonceImageView)
            annonce?.image?.let {
                val bundle = Bundle()
                bundle.putString("guid", annonce?.id)
                root.setOnClickListener(
                    Navigation.createNavigateOnClickListener(
                        R.id.action_homeFragment_to_fullScreenImageFragment,
                        bundle
                    )
                )
            }


        }
    }

    class MyViewHolder(val binding: AnnonceBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnFavAnnonceChangedListener {
        fun onFavChanged(guid: String, value: Boolean)
    }

}