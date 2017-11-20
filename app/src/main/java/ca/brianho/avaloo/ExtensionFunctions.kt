package ca.brianho.avaloo

import android.app.Activity
import android.app.Fragment

fun Activity.replaceFragment(frameId: Int, fragment: Fragment) {
    fragmentManager.beginTransaction().replace(frameId, fragment).addToBackStack(null).commit()
}

