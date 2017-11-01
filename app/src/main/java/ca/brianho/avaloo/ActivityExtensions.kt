package ca.brianho.avaloo

import android.app.Activity
import android.app.Fragment

/**
 * Created by brianho on 2017-11-01.
 */

fun Activity.replaceFragment(frameId: Int, fragment: Fragment) {
    fragmentManager.beginTransaction().replace(frameId, fragment).commit()
}

