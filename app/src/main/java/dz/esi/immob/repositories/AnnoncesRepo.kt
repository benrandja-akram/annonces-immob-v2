package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class AnnoncesRepo private constructor() {

    companion object {
        val instance = AnnoncesRepo()
    }

    var mFeed = MutableLiveData<List<Annonce>>()
    val db = FirebaseFirestore.getInstance()

    var userRepo = UserData.instance

    init {
        getFeed()
    }

    fun getFeed(): MutableLiveData<List<Annonce>> {
        if (mFeed.value == null) {
            db.collection("annonces")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val annoces = value?.toObjects(Annonce::class.java)
                    userRepo.getFavAnnonces().value?.forEach { fav ->
                        annoces?.forEach { annonce ->
                            if (fav.id == annonce.id) {
                                annonce.favorite = 1
                            }
                        }
                    }
                    mFeed.postValue(annoces)
                }
        }
        return mFeed
    }

    fun findAnnonceById(id: String, consume: Consumer){
        Log.e("exceptionannonce", id)

        db.collection("annonces")
            .document(id)
            .get()
            .addOnSuccessListener{ value ->
                consume(value.toObject(Annonce::class.java))
            }
            .addOnFailureListener{
                Log.e("exceptionannonce", it.toString())
            }

    }

    fun filter(title: String): List<Annonce> {

        val items = mFeed.value?.filter {annonce ->
            annonce.title?.toLowerCase()?.contains(title.toLowerCase()) ?: false
        } ?: listOf()

        return items

//        return items
//        val ref = db.collection("annonces")
//        var query: Query? = null
//        val annonces = MutableLiveData<List<Annonce>>()
//
//        for ((key, value) in criteria) {
//            query = ref.whereEqualTo(key, value)
//        }
//        query?.get(Source.CACHE)
//            ?.addOnSuccessListener {
//                annonces.postValue(it.toObjects(Annonce::class.java))
//            }
//        return annonces
    }

}
typealias Consumer = (Annonce?) -> Unit


