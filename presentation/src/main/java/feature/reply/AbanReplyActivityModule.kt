/*
 * Copyright (c) 2018. Arash Hatami
 */

package feature.reply

import android.arch.lifecycle.ViewModel
import android.content.Intent
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import injection.ViewModelKey

@Module
class AbanReplyActivityModule {

    @Provides
    fun provideIntent(activity: AbanReplyActivity): Intent = activity.intent

    @Provides
    @IntoMap
    @ViewModelKey(AbanReplyViewModel::class)
    fun provideAbanReplyViewModel(viewModel: AbanReplyViewModel): ViewModel = viewModel

}