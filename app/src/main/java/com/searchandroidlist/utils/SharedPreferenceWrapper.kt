package com.searchandroidlist.utils

import android.content.SharedPreferences

abstract class SharedPreferenceWrapper {

    lateinit var prefs: SharedPreferences

    fun isInitialized() = ::prefs.isInitialized

    open fun read(key: String, defValue: Any): Any? {
        return when (defValue) {
            is String -> prefs.getString(key, defValue)
            is Int -> prefs.getInt(key, defValue)
            is Boolean -> prefs.getBoolean(key, defValue)
            is Long -> prefs.getLong(key, defValue)
            is Float -> prefs.getFloat(key, defValue)
            is Set<*> -> {
                if (defValue.isNotEmpty() && defValue.elementAt(0) is String) prefs.getStringSet(
                    key,
                    defValue as Set<String>
                )
                else return null
            }
            else -> null
        }
    }

    open fun write(key: String, value: Any): Any? {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Set<*> -> {
                    if (value.isNotEmpty() && value.elementAt(0) is String) putStringSet(
                        key,
                        value as Set<String>
                    )
                    else return null
                }
                else -> return null
            }
            commit()
        }
        return value
    }
}