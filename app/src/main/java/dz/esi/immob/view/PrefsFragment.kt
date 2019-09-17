package dz.esi.immob.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.firebase.ui.auth.AuthUI
import dz.esi.immob.MainActivity
import dz.esi.immob.R
import dz.esi.immob.repositories.UserData
import dz.esi.immob.repositories.UserPreferenceDataStore

class PrefsFragment : PreferenceFragmentCompat() {
    private val dataStore = UserPreferenceDataStore()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val preference = findPreference<ListPreference>("wilaya")
        preference?.preferenceDataStore = dataStore

        println(UserData.instance.wilaya.value)

        preference?.value = UserData.instance.wilaya.value
        preference?.summary = UserData.instance.wilaya.value


        UserData.instance.getFavWilaya().observe(this, Observer { wilaya ->
            preference?.value = wilaya
            preference?.summary = dataStore.getString("wilaya", "")
        })

    }

}