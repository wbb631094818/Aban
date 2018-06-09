/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class AbanViewModel<in View : AbanView<State>, State>(initialState: State) : ViewModel() {

    protected val state: BehaviorSubject<State> = BehaviorSubject.createDefault(initialState)

    protected val disposables = CompositeDisposable()

    @CallSuper
    open fun bindView(view: View) {
        state
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(view.scope())
                .subscribe { view.render(it) }
    }

    protected fun newState(reducer: (State) -> State) {
        state.value?.let { state.onNext(reducer(it)) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}