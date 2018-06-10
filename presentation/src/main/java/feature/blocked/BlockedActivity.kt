/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.blocked

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import com.franmontiel.localechanger.LocaleChanger
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.base.AbanThemedActivity
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.blocked_activity.*
import kotlinx.android.synthetic.main.settings_switch_widget.view.*
import java.util.*
import javax.inject.Inject

class BlockedActivity : AbanThemedActivity(), BlockedView {

    @Inject lateinit var blockedAdapter: BlockedAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val siaClickedIntent by lazy { shouldIAnswer.clicks() }
    override val unblockIntent by lazy { blockedAdapter.unblock }
    override val confirmUnblockIntent: Subject<Unit> = PublishSubject.create()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[BlockedViewModel::class.java] }

    override fun attachBaseContext(newBase: Context) {
        var base = newBase
        base = LocaleChanger.configureBaseContext(base)
        super.attachBaseContext(base)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        LocaleChanger.setLocale(Locale("fa", "IR"));
        setContentView(R.layout.blocked_activity)
        setTitle(R.string.blocked_title)
        showBackButton(true)
        viewModel.bindView(this)

        blockedAdapter.emptyView = empty
        conversations.adapter = blockedAdapter
    }

    override fun render(state: BlockedState) {
        shouldIAnswer.checkbox.isChecked = state.siaEnabled

        blockedAdapter.updateData(state.data)
    }

    override fun showUnblockDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.blocked_unblock_dialog_title)
                .setMessage(R.string.blocked_unblock_dialog_message)
                .setPositiveButton(R.string.button_unblock, { _, _ -> confirmUnblockIntent.onNext(Unit) })
                .setNegativeButton(R.string.button_cancel, null)
                .show()
    }

}