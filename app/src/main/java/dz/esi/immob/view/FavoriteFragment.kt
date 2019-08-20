package dz.esi.immob.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import dz.esi.immob.R
import dz.esi.immob.adapters.AnnoncesAdapter
import dz.esi.immob.repositories.Annonce
import dz.esi.immob.view.viewmodel.AnnoncesViewModel

class FavoriteFragment : Fragment(), Observer<List<Annonce>>, AnnoncesAdapter.OnFavAnnonceChangedListener {
    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this)[AnnoncesViewModel::class.java]
        model.favorites.observe(this, this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.annonces_recycler)?.adapter = AnnoncesAdapter(model.favorites.value, this)
    }

    override fun onChanged(feed: List<Annonce>?) {
        Log.i("onchanged", feed?.size.toString())
        feed ?: return

        view?.apply {
            findViewById<RecyclerView>(R.id.annonces_recycler)?.adapter?.apply {
                this as AnnoncesAdapter
                annonces = feed
                notifyDataSetChanged()
            }
        }

    }

    override fun onFavChanged(id: String, value: Boolean) {
        if (value) {
            model.addFavAnnonce(id)
        } else {
            model.deleteFavAnnonce(id)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        view?.findViewById<RecyclerView>(R.id.annonces_recycler)?.layoutManager?.onRestoreInstanceState(model.state)
        Log.i("recycle_annoncess", "onViewStateRestored")
    }

    override fun onPause() {
        super.onPause()
        println("favorite")
        model.state = view?.findViewById<RecyclerView>(R.id.annonces_recycler)?.layoutManager?.onSaveInstanceState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as? AppCompatActivity)?.supportActionBar?.hide()

    }
}
