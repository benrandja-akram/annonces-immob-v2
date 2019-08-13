package dz.esi.immob.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import dz.esi.immob.R
import dz.esi.immob.adapters.AnnoncesAdapter
import dz.esi.immob.api.annonce.Feed
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.repositories.UserData
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), Observer<List<Annonce>>, AnnoncesAdapter.OnFavAnnonceChangedListener {
    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity!!)[AnnoncesViewModel::class.java]
        model.feed.observe(activity!!, this)


        val items = model.filter(mapOf("wilaya" to "Oran"))
        Handler().postDelayed({
            this.onChanged(items.value)
        }, 10000)


        UserData.instance.setFavWilaya(FirebaseAuth.getInstance().currentUser?.uid!!, "Ain defla")
        UserData.instance.getFavWilaya(FirebaseAuth.getInstance().currentUser?.uid!!)
            .observe(this, Observer {
                Log.i("onchanged-wilaya", it)

            })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.annonces_recycler.adapter = AnnoncesAdapter(model.feed.value, this)


    }

    override fun onDetach() {
        Log.i("onDetach", model.feed.value?.size.toString())
        super.onDetach()
    }

    override fun onChanged(feed: List<Annonce>?) {
        Log.i("onchanged", feed?.size.toString())
        feed ?: return

        view?.apply {
            annonces_recycler?.adapter?.apply {
                this as AnnoncesAdapter
                annonces = feed
                notifyDataSetChanged()
            }
        }

    }

    override fun onFavChanged(guid: String, value: Boolean) {
        if (value) {
            model.addFavAnnonce(guid)
        } else {
            model.deleteFavAnnonce(guid)
        }
    }
}
