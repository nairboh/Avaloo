package ca.brianho.avaloo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.BoardFragment
import ca.brianho.avaloo.fragments.game.RoleFragment
import ca.brianho.avaloo.models.MessageType
import ca.brianho.avaloo.network.WSConnection
import ca.brianho.avaloo.utils.RxEventBus
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.*
import org.json.JSONObject

class GameActivity : AppCompatActivity() {
    private val boardFragment = BoardFragment()
    private val roleFragment = RoleFragment()

    private lateinit var rxBusDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setupFragmentContainers()
        setupBottomNavBar()
        rxBusDisposable = RxEventBus.subscribe(Consumer { handleResponseMessage(it) })
    }

    private fun setupFragmentContainers() {
        fragmentManager.beginTransaction().replace(R.id.boardFragmentContainer, boardFragment).commit()
        fragmentManager.beginTransaction().replace(R.id.roleFragmentContainer, roleFragment).commit()
        fragmentManager.executePendingTransactions()
    }

    private fun setupBottomNavBar() {
        bottomNav.setOnNavigationItemSelectedListener { handleNavigationItemSelected(it) }
        bottomNav.selectedItemId = R.id.board
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::rxBusDisposable.isInitialized) {
            rxBusDisposable.dispose()
        }
    }

    private fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.CLIENT_SETUP.name -> {
                boardFragment.handleResponseMessage(message)
                roleFragment.handleResponseMessage(message)
            }
            MessageType.QUEST_INFO.name -> boardFragment.handleResponseMessage(message)
            MessageType.PARTY_VOTE.name -> boardFragment.handleResponseMessage(message)
            MessageType.PARTY_RESULT.name -> boardFragment.handleResponseMessage(message)
            MessageType.CHOOSE_TARGET.name -> boardFragment.handleResponseMessage(message)
            MessageType.GAME_OVER.name -> boardFragment.handleResponseMessage(message)
        }
    }

    private fun handleNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.board -> {
                roleFragmentContainer.visibility = View.GONE
                boardFragmentContainer.visibility = View.VISIBLE
            }
            R.id.role -> {
                boardFragmentContainer.visibility = View.GONE
                roleFragmentContainer.visibility = View.VISIBLE
            }
            else -> throw Error ("Unsupported View")
        }
        return true
    }

    override fun onBackPressed() {
        alert(getString(R.string.message_leave_game_confirm),
                getString(R.string.message_leave_game_confirm_title)) {
            yesButton {
                WSConnection.disconnect()
                RxEventBus.clear()
                startActivity(intentFor<LobbyActivity>().newTask().clearTask())
            }
            noButton {}
        }.show()
    }
}
