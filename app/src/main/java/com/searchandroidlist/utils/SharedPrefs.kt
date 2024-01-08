package com.searchandroidlist.utils

import android.content.Context

object SharedPrefs : SharedPreferenceWrapper() {
    private val validKeys: List<String> = listOf(
        APP_USER_ID, APP_PASSWORD, APP_TOKEN,
        "pref_delay_t1", "pref_delay_t2",
        "pref_delay_v1", "pref_delay_v2",
        "pref_main_email"
    )

    //call it once
    fun init(context: Context, pref_name: String = "", mode: Int = Context.MODE_PRIVATE) {

        if (isInitialized()) return      // prevent multiple init

        prefs = context.getSharedPreferences(pref_name, mode)
    }

    override fun read(key: String, defValue: Any): Any? {
        return if (!validKeys.contains(key) || !isInitialized()) null
        else super.read(key, defValue)
    }

    override fun write(key: String, value: Any): Any? {
        return if (!validKeys.contains(key) || !isInitialized()) null
        else super.write(key, value)
    }
}