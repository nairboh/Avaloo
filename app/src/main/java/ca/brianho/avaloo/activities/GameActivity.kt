package ca.brianho.avaloo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.boardFragment
import ca.brianho.avaloo.utils.replaceFragment
import ca.brianho.avaloo.utils.roleFragment
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        bottomNav.setOnNavigationItemSelectedListener { handleNavigationItemSelected(it) }
    }

    private fun handleNavigationItemSelected(item: MenuItem): Boolean {
        replaceFragment(R.id.fragment_container, when (item.itemId) {
            R.id.board -> boardFragment
            R.id.role -> roleFragment
            else -> throw Error ("Unsupported View")
        })
        return true
    }
}
