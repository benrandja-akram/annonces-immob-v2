package dz.esi.immob.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dz.esi.immob.MainActivity
import dz.esi.immob.R
import kotlinx.android.synthetic.main.full_screen_image_fragment.view.*
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.view.*

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.logout.setOnClickListener{
            AuthUI.getInstance().signOut(context!!)
                .addOnSuccessListener {
                    println("signed out with success")
                    startActivity(Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    })
                }
                .addOnFailureListener {
                    println("signed out with failure")
                }
        }
        view.username.text = FirebaseAuth.getInstance().currentUser?.displayName




        Glide.with(this)
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .circleCrop()
            .error(R.drawable.no_image_available)
            .circleCrop()
            .into(view.userPhoto)

    }
}