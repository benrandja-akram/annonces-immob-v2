package dz.esi.immob

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dz.esi.immob.repositories.UserData
import dz.esi.immob.services.CHANNEL_ID
import dz.esi.immob.services.FcmIntentService
import dz.esi.immob.view.FilterDialogFragment
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.data
import com.firebase.ui.auth.IdpResponse
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import androidx.navigation.NavDestination
import kotlinx.android.synthetic.main.activity_annonce_details.*


class MainActivity : AppCompatActivity() {
    lateinit var model: AnnoncesViewModel

    private val RC_SIGN_IN_FIREBASE = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.progress)
        if(!isAuthenticated){
            auth()
        }
        else{
            setupUi()
        }
    }

    private val isAuthenticated
        get() = FirebaseAuth.getInstance().currentUser != null

    private fun auth(){

        println("auth-service started")
        FirebaseAuth.getInstance().signOut()

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
//            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.home_256)
                .build(),
            RC_SIGN_IN_FIREBASE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN_FIREBASE) {
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Log.i("user-signed", user.toString())
                setupUi()
//                startMainActivity()
            } else {
                println("auth-service error ")
                val response = IdpResponse.fromResultIntent(data)
                println(response?.error?.errorCode)
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    fun setupUi(){
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(findNavController(R.id.main_nav_host_fragment))
        setSupportActionBar(mainToolbar)
        mainToolbar.setNavigationIcon(R.drawable.logo_app)
        findNavController(R.id.main_nav_host_fragment).navigate(R.id.home_fragment)
        model = ViewModelProviders.of(this)[AnnoncesViewModel::class.java]

        model.feed.observe(this, Observer {
            model.updateItems()
        })
        onSearchRequested()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_actions, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setIconifiedByDefault(false)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    model.filter(query?.trim() ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    findNavController(R.id.main_nav_host_fragment).navigate(R.id.home_fragment)
                    model.filter(newText?.trim() ?: "")
                    return true
                }
            })
            setOnCloseListener {
                model.cancelFilter()
                return@setOnCloseListener false
            }
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val filterDialog = FilterDialogFragment()

        when(item.itemId){
            R.id.filter -> {
                println("filtring ... ")
                filterDialog.show(supportFragmentManager, "filter-dialog")
                findNavController(R.id.main_nav_host_fragment).navigate(R.id.home_fragment)

                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
