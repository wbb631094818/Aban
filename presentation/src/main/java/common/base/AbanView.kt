/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.base

import android.arch.lifecycle.LifecycleOwner

interface AbanView<in State> : LifecycleOwner {

    fun render(state: State)

}