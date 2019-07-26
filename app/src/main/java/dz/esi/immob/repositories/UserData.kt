package dz.esi.immob.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import dz.esi.immob.api.annonce.Annonce

class UserData {
    val db = FirebaseFirestore.getInstance()

    fun setFavWilaya(id: String, context: Context, wilaya: String = "Alger"){
        db.collection("users").document(id).set(User(wilaya), SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Success ", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener{e->
                throw e
            }
    }

    fun addFavAnnonce(userId: String, mAnnonce: Annonce?){
        Log.i("mAnnonce", mAnnonce.toString())
        mAnnonce?.let { annonce ->
            db.collection("users")
                .document(userId)
                .collection("annonces")
                .document(annonce.guid!!.replace("/", "-"))
                .set(annonce)
                .addOnSuccessListener { documentReference ->
                    Log.i("addFavAnnonce", "prefred annocne $annonce")
                }
                .addOnFailureListener{e->
                    throw e
                }
        }
    }
    fun deleteFavAnnonce(userId: String, guid: String){
        db.collection("users")
            .document(userId)
            .collection("annonces")
            .document(guid.replace("/", "-"))
            .delete()
            .addOnSuccessListener { documentReference ->
                Log.i("deletedPrefredAnnonce", "deleted annocne")
            }
            .addOnFailureListener{e->
                throw e
            }
    }
    fun getFavAnnonces(success: OnSuccessListener, failure: OnFailureListener){
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .collection("annonces")
            .get()
            .addOnSuccessListener {query ->
               success(query)
            }.addOnFailureListener{ex ->
                failure(ex)
            }
    }

}

data class User(val wilaya: String? = null)

typealias OnSuccessListener = (QuerySnapshot) -> Unit
typealias OnFailureListener = (Exception) -> Unit