package ca.brianho.avaloo


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_name.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
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
            val name = nameField.text.toString()
            defaultSharedPreferences.edit().putString(getString(R.string.key_name), name).apply()
            info("Name for client set to " + name)

            activity.replaceFragment(R.id.fragment_container, NameFragment())
        }
    }
}
