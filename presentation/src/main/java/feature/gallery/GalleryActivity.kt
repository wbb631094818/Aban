/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.gallery

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionSet
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.clicks
import ir.hatamiarash.aban.R
import common.GlideCompletionListener
import common.base.AbanActivity
import common.util.GlideApp
import common.util.extensions.setVisible
import dagger.android.AndroidInjection
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.gallery_activity.*
import javax.inject.Inject

class GalleryActivity : AbanActivity(), GalleryView {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val screenTouchedIntent by lazy { image.clicks() }
    override val optionsItemSelectedIntent: Subject<Int> = PublishSubject.create()

    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory)[GalleryViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_activity)
        postponeEnterTransition()
        showBackButton(true)
        viewModel.bindView(this)

        val transition = TransitionSet().apply {
            duration = 100

            addTransition(ChangeBounds())
            addTransition(ChangeImageTransform())
        }

        window.sharedElementReturnTransition = transition
        window.sharedElementEnterTransition = transition

        image.transitionName = intent.getLongExtra("partId", 0L).toString()

        // When calling the public setter, it doesn't allow the midscale to be the same as the
        // maxscale or the minscale. We don't want 3 levels and we don't want to modify the library
        // so let's celebrate the invention of reflection!
        image.attacher.run {
            javaClass.getDeclaredField("mMinScale").run {
                isAccessible = true
                setFloat(image.attacher, 1f)
            }
            javaClass.getDeclaredField("mMidScale").run {
                isAccessible = true
                setFloat(image.attacher, 1f)
            }
            javaClass.getDeclaredField("mMaxScale").run {
                isAccessible = true
                setFloat(image.attacher, 3f)
            }
        }
    }

    override fun render(state: GalleryState) {
        toolbar.setVisible(state.navigationVisible)

        title = state.title

        if (image.drawable == null) {
            GlideApp.with(this)
                    .load(state.imageUri)
                    .dontAnimate()
                    .listener(GlideCompletionListener {
                        startPostponedEnterTransition()
                    })
                    .into(image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gallery, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> optionsItemSelectedIntent.onNext(item.itemId)
        }
        return true
    }

}