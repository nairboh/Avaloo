package ca.brianho.avaloo.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import ca.brianho.avaloo.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
    }
}
