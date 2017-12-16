package ca.brianho.avaloo.utils

import android.app.Activity
import android.app.Fragment

fun Activity.replaceFragment(frameId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    if (addToBackStack) {
        fragmentManager.beginTransaction()
                       .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                               android.R.animator.fade_in, android.R.animator.fade_out)
                       .replace(frameId, fragment).addToBackStack(null).commit()
    } else {
        fragmentManager.beginTransaction().replace(frameId, fragment).commit()
    }
}