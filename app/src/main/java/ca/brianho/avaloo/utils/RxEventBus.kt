package ca.brianho.avaloo.utils

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject

object RxEventBus {
    private lateinit var bus: ReplaySubject<String>

    private fun checkInit() {
        if (!RxEventBus::bus.isInitialized) {
            bus = ReplaySubject.create()
        }
    }

    fun onNext(message: String) {
        checkInit()
        bus.onNext(message)
    }

    fun subscribe(handler: Consumer<String>): Disposable {
        checkInit()
        return bus.subscribeOn(Schedulers.io()).subscribe(handler)
    }

    fun clear() {
        bus = ReplaySubject.create()
    }
}