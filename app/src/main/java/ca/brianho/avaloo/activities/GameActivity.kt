package ca.brianho.avaloo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.BoardFragment
import ca.brianho.avaloo.fragments.game.RoleFragment
import ca.brianho.avaloo.network.WSConnection
import ca.brianho.avaloo.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class GameActivity : AppCompatActivity() {
    private lateinit var boardFragment: BoardFragment
    private lateinit var roleFragment: RoleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        switchToBoardFragment()
        bottomNav.setOnNavigationItemSelectedListener { handleNavigationItemSelected(it) }
    }

    private fun handleNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.board -> switchToBoardFragment()
            R.id.role -> switchToRoleFragment()
            else -> throw Error ("Unsupported View")
        }
        return true
    }

    fun switchToBoardFragment() {
        if (!::boardFragment.isInitialized) {
            boardFragment = BoardFragment()
        }
        replaceFragment(R.id.fragment_container, boardFragment)
    }

    private fun switchToRoleFragment() {
        if (!::roleFragment.isInitialized) {
            roleFragment = RoleFragment()
        }
        replaceFragment(R.id.fragment_container, roleFragment)
    }

    override fun onBackPressed() {
        alert(getString(R.string.message_leave_game_confirm),
                getString(R.string.message_leave_game_confirm_title)) {
            yesButton {
                WSConnection.disconnect()
                super.onBackPressed()
            }
            noButton {}
        }.show()
    }
}
