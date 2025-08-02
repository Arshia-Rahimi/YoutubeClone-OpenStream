package com.github.openstream.core.common.android

import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

fun Context.restartApp() {
    val intent = packageManager.getLaunchIntentForPackage(packageName)
        ?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    exitProcess(0)
}
