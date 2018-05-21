package ca.brianho.avaloo2.utils

import ca.brianho.avaloo2.network.WSConnection
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi

object MoshiInstance {
    private lateinit var moshi: Moshi

    fun get(): Moshi {
        if (!MoshiInstance::moshi.isInitialized) {
            moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }

        return moshi
    }

    inline fun <reified T>sendRequestAsJson(t: T) {
        val adapter = get().adapter<T>(T::class.java)
        WSConnection.send(adapter.toJson(t))
    }

    inline fun <reified T>fromJson(json: String): T {
        val adapter = get().adapter<T>(T::class.java)
        return adapter.fromJson(json) ?: throw NullPointerException("Unable to deserialize Json!")
    }

    inline fun <reified T>toJson(t: T): String {
        val adapter = get().adapter<T>(T::class.java)
        return adapter.toJson(t)
    }
}