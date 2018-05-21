package ca.brianho.avaloo2.fragments

import android.os.Bundle
import android.preference.PreferenceFragment
import ca.brianho.avaloo2.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_preferences)
    }
}
