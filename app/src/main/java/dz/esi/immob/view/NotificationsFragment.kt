package dz.esi.immob.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dz.esi.immob.AnnonceDetails

import dz.esi.immob.R
import dz.esi.immob.adapters.NotificationsAdapter
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.view.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*


class NotificationsFragment : Fragment() {
    lateinit var model: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this)[NotificationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.notificationsRecycler.adapter = NotificationsAdapter(model.notifications.value).apply {
            onAnnonceClicked = this@NotificationsFragment.onNotificationClicked
        }

        model.notifications.observe(this, observer)
    }

    private val observer = Observer<List<Annonce>> { annonces ->
        Log.i("observernotif", annonces.toString())
        annonces ?: return@Observer

        view?.notificationsRecycler?.adapter = NotificationsAdapter(annonces).apply {
            onAnnonceClicked = this@NotificationsFragment.onNotificationClicked
        }
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        view?.notificationsRecycler?.layoutManager?.onRestoreInstanceState(model.state)
    }

    override fun onPause() {
        super.onPause()
        model.state = view?.notificationsRecycler?.layoutManager?.onSaveInstanceState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    val onNotificationClicked = object : NotificationsAdapter.OnAnnonceClicked {
        override fun onClicked(id: String?) {
            this@NotificationsFragment.startActivity(
                Intent(context, AnnonceDetails::class.java).apply {
                    putExtra("id", id)
                }
            )
        }
    }

}
