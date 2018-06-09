/*
 * Copyright (c) 2018. Arash Hatami
 */

package feature.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return WidgetAdapter(intent)
    }

}
