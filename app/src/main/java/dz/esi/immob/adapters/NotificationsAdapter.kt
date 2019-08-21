package dz.esi.immob.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dz.esi.immob.R
import dz.esi.immob.databinding.NotificationBinding
import dz.esi.immob.repositories.Annonce

class NotificationsAdapter(val notifications: List<Annonce>?) : RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>(){
    class MyViewHolder( val binding: NotificationBinding): RecyclerView.ViewHolder(binding.root)
    var onAnnonceClicked : OnAnnonceClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.notification,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = notifications?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val notif = notifications?.get(position)
        holder.binding.annonce = notif
        Glide.with(holder.binding.root)
            .load(notif?.image)
            .circleCrop()
            .error(R.drawable.no_image_available)
            .into(holder.binding.image)
        holder.binding.root.setOnClickListener{
            onAnnonceClicked?.onClicked(notif?.id)
        }
    }

    interface OnAnnonceClicked {
        fun onClicked(id: String?)
    }

}