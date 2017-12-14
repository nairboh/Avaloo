package ca.brianho.avaloo.utils

import android.app.Activity
import android.app.Fragment
import com.squareup.moshi.Moshi
import okhttp3.WebSocket

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

inline fun <reified T>WebSocket.sendJson(t: T) {
    val adapter = moshi.adapter<T>(T::class.java)
    this.send(adapter.toJson(t))
}

inline fun <reified T> Moshi.fromJson(json: String?): T {
    if (json.isNullOrBlank()) {
        throw NullPointerException("Json is blank!")
    } else {
        val adapter = this.adapter<T>(T::class.java)
        return adapter.fromJson(json) ?:
                throw NullPointerException("Unable to deserialize Json!")
    }
}