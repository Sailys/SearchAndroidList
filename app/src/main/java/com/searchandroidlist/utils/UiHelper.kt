package com.searchandroidlist.utils

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar

class UiHelper {
    companion object {
        fun showSnackBar(view: View, msg: String) {
            val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            snack.show()
        }

        fun showLogs(tag:String,msg: String) {
            Log.e(tag, msg)
        }
    }
}