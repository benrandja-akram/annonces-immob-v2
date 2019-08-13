package dz.esi.immob.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class UserData private constructor() {
    val db = FirebaseFirestore.getInstance()
    var annonces = MutableLiveData<List<Annonce>>()
    val wilaya = MutableLiveData<String>()

    companion object {
        val instance = UserData()
    }

    init {
        getFavAnnonces()
    }

    fun setFavWilaya(id: String, wilaya: String) {
        db.collection("users")
            .document(id)
            .set(User(wilaya), SetOptions.merge())
    }

    fun getFavWilaya(id: String): MutableLiveData<String> {

        if(wilaya.value == null) {
            db.collection("users")
                .document(id)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    wilaya.postValue(value?.toObject(User::class.java)?.wilaya)
                }
        }

        return wilaya
    }

    fun addFavAnnonce(userId: String, mAnnonce: Annonce?) {
        Log.i("mAnnonce", mAnnonce.toString())
        mAnnonce?.let { annonce ->
            db.collection("users")
                .document(userId)
                .collection("annonces")
                .document(annonce.id!!.replace("/", "-"))
                .set(annonce)
                .addOnSuccessListener { documentReference ->
                    Log.i("addFavAnnonce", "prefred annocne $annonce")
                }
                .addOnFailureListener { e ->
                    throw e
                }
        }
    }

    fun deleteFavAnnonce(userId: String, guid: String) {

        db.collection("users")
            .document(userId)
            .collection("annonces")
            .document(guid.replace("/", "-"))
            .delete()
            .addOnSuccessListener { documentReference ->
                Log.i("deletedPrefredAnnonce", "deleted annocne")
            }
            .addOnFailureListener { e ->
                throw e
            }

    }

    fun getFavAnnonces(): LiveData<List<Annonce>> {

        if (annonces.value == null) {
            db.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .collection("annonces")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    annonces.postValue(value?.toObjects(Annonce::class.java))
                }
        }

        return annonces
    }

}

data class User(val wilaya: String? = null)
