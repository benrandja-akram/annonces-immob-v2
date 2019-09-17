package dz.esi.immob.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dz.esi.immob.R
import dz.esi.immob.databinding.NotificationBinding
import dz.esi.immob.repositories.Annonce
import kotlinx.android.synthetic.main.contact.view.*
import me.everything.providers.android.contacts.Contact
import me.everything.providers.core.Data

class ContactsAdapter( val contacts: List<Contact>?) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>(){
    class MyViewHolder( val view: View): RecyclerView.ViewHolder(view)
    var onContactSelectedListener : OnContactSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact, parent, false))
    }

    override fun getItemCount() = contacts?.size ?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts?.get(position)

        holder.view.name.text = contact?.displayName
        holder.view.phone.text = contact?.phone

        holder.view.contactCard.setOnClickListener{

            onContactSelectedListener?.onClicked(contact!!)
        }
    }

    interface OnContactSelectedListener {
        fun onClicked(contact: Contact)
    }

}