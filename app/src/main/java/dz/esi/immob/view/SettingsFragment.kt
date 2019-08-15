package dz.esi.immob.view

import android.app.Activity
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
import dz.esi.immob.R
import dz.esi.immob.repositories.UserData
import dz.esi.immob.repositories.UserPreferenceDataStore
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment: PreferenceFragmentCompat() {
    private val dataStore = UserPreferenceDataStore()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val preference = findPreference<ListPreference>("wilaya")
        preference?.preferenceDataStore = dataStore
        UserData.instance.wilaya.observe(this, Observer {wilaya ->
            preference?.value = wilaya
            preference?.summary = dataStore.getString("wilaya", "")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }
}