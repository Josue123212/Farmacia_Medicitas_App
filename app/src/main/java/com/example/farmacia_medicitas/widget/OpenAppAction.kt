package com.example.farmacia_medicitas.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.example.farmacia_medicitas.MainActivity

class OpenAppAction : ActionCallback {
    companion object {
        val ROUTE_KEY: ActionParameters.Key<String> = ActionParameters.Key("route")
    }

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val route = parameters[ROUTE_KEY]
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (route != null) putExtra(MainActivity.EXTRA_NAV_ROUTE, route)
        }
        context.startActivity(intent)
    }
}
