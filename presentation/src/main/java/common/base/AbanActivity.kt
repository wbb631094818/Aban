/*
 * Copyright (c) 2018. Arash Hatami
 */
package common.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.franmontiel.localechanger.LocaleChanger
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

abstract class AbanActivity : AppCompatActivity() {

    protected val menu: Subject<Menu> = BehaviorSubject.create()

    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleChanger.setLocale(Locale("fa", "IR"));
        onNewIntent(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        var base = newBase
        base = LocaleChanger.configureBaseContext(base)
        base = CalligraphyContextWrapper.wrap(base)
        super.attachBaseContext(base)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setSupportActionBar(toolbar)
        title = title // The title may have been set before layout inflation
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        toolbarTitle?.text = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        if (menu != null) {
            this.menu.onNext(menu)
        }
        return result
    }

    protected open fun showBackButton(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

}