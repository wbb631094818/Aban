/*
 * Copyright (c) 2018. Arash Hatami
 */

package feature.settings.about

import common.base.AbanView
import common.widget.PreferenceView
import io.reactivex.subjects.Subject

interface AboutView : AbanView<Unit> {

    val preferenceClickIntent: Subject<PreferenceView>

}