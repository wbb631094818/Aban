/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.reply

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import ir.hatamiarash.aban.R
import ir.hatamiarash.aban.R.id.*
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import common.base.AbanThemedActivity
import common.util.extensions.autoScrollToStart
import common.util.extensions.setBackgroundTint
import common.util.extensions.setTint
import common.util.extensions.setVisible
import dagger.android.AndroidInjection
import feature.compose.MessagesAdapter
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.qkreply_activity.*
import javax.inject.Inject

class AbanReplyActivity : AbanThemedActivity(), AbanReplyView {

    @Inject lateinit var adapter: MessagesAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val menuItemIntent: Subject<Int> = PublishSubject.create()
    override val textChangedIntent by lazy { message.textChanges() }
    override val sendIntent by lazy { send.clicks() }

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[AbanReplyViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        setFinishOnTouchOutside(prefs.qkreplyTapDismiss.get())
        setContentView(R.layout.qkreply_activity)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewModel.bindView(this)

        theme
                .autoDisposable(scope())
                .subscribe { theme ->
                    send.setBackgroundTint(theme.theme)
                    send.setTint(theme.textPrimary)
                }

        toolbar.clipToOutline = true

        adapter.autoScrollToStart(messages)

        messages.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        messages.adapter = adapter
    }

    override fun render(state: AbanReplyState) {
        if (state.hasError) {
            finish()
        }

        threadId.onNext(state.data?.first?.id ?: 0)

        title = state.title

        toolbar.menu.findItem(R.id.expand)?.isVisible = !state.expanded
        toolbar.menu.findItem(R.id.collapse)?.isVisible = state.expanded

        adapter.data = state.data

        counter.text = state.remaining
        counter.setVisible(counter.text.isNotBlank())

        send.isEnabled = state.canSend
        send.imageAlpha = if (state.canSend) 255 else 128
    }

    override fun setDraft(draft: String) {
        message.setText(draft)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qkreply, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuItemIntent.onNext(item.itemId)
        return true
    }

    override fun getActivityThemeRes(night: Boolean, black: Boolean) = when {
        night && black -> R.style.AppThemeBlackDialog
        night && !black -> R.style.AppThemeDarkDialog
        else -> R.style.AppThemeLightDialog
    }

}