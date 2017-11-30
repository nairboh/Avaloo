package ca.brianho.avaloo.activities

import android.app.Activity
import android.os.Bundle
import ca.brianho.avaloo.R

class GameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }
}
