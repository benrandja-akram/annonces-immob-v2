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

class MainActivity : AppCompatActivity() {
    lateinit var model: AnnoncesViewModel

    private val RC_SIGN_IN_FIREBASE = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        model = ViewModelProviders.of(this)[AnnoncesViewModel::class.java]
        if(!isAuthenticated()){
            auth()
        }
        else{
            setupUi()
        }
    }

    private fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null

    private fun auth(){

        UserData.instance.subscribeToTopic("newAnnonce", Runnable {
            startService(Intent(this, FcmIntentService::class.java))
            println("subscribed ... ")
        })

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
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
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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
