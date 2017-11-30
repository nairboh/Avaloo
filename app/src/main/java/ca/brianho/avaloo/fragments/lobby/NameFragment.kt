package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.name
import ca.brianho.avaloo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_name.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class NameFragment : Fragment(), AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_name, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton.setOnClickListener { onNextClicked() }
    }

    private fun onNextClicked() {
        if (nameField.text.isNullOrBlank()) {
            toast("Please enter a name to continue")
        } else {
            name = nameField.text.toString()
            defaultSharedPreferences.edit().putString(getString(R.string.key_name), name).apply()
            info("Name for client set to " + name)

            activity.replaceFragment(R.id.fragment_container, CreateOrJoinFragment())
        }
    }
}