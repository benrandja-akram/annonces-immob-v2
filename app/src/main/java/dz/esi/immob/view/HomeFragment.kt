package dz.esi.immob.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dz.esi.immob.R
import dz.esi.immob.adapters.AnnoncesAdapter
import dz.esi.immob.api.annonce.AnnonceController
import dz.esi.immob.api.annonce.Feed
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), Observer<Feed>{
    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity!!)[AnnoncesViewModel::class.java]
        model.refreshFeed()
        model.feed.observe(activity!!, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.annonces_recycler.adapter = AnnoncesAdapter(model.feed.value?.items)
        view.annonces_swipe_refresh?.setOnRefreshListener {
            model.refreshFeed()
        }
    }

    override fun onDetach() {
        Log.i("onDetach", model.feed.value?.items?.size.toString() )
        super.onDetach()
    }

    override fun onChanged(feed: Feed?) {
        Log.i("onchanged", feed?.items?.size.toString() ?: "0")
        feed?.items ?: return

        view?.apply {
            annonces_swipe_refresh?.isRefreshing = false
            annonces_recycler?.adapter?.apply {
                this as AnnoncesAdapter
                annonces = feed.items
                notifyDataSetChanged()
            }
        }

    }
}
