package dz.esi.immob.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dz.esi.immob.R
import dz.esi.immob.adapters.AnnoncesAdapter
import dz.esi.immob.api.annonce.AnnonceController
import dz.esi.immob.api.annonce.Feed
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), Observer<Feed>{
    var  listener: OnFragmentInteractionListener? = null
    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity!!)[AnnoncesViewModel::class.java]
        model.refreshFeed()
        model.feed.observe(this, this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.annonces_swipe_refresh.setOnRefreshListener {
            model.refreshFeed()
        }
        view.annonces_recycler.adapter = AnnoncesAdapter(null)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onChanged(feed: Feed?) {
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
