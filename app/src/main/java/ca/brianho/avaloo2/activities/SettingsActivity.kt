package ca.brianho.avaloo2.activities

import android.os.Bundle
import android.preference.PreferenceActivity
import ca.brianho.avaloo2.fragments.SettingsFragment

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment()).commit()
    }
}
