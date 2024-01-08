package com.searchandroidlist.utils

import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Validation {
    companion object {
        fun checkValidEmailAddress(textInput: TextInputEditText): String? {
            val emailText = textInput.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                return "Invalid Email Address "
            }
            return null
        }

        fun checkValidPhoneNumber(textInput: TextInputEditText): String? {
            val phoneText = textInput.text.toString()
            if (!phoneText.matches(".*[0-9].*".toRegex())) {
                return "Must be all Digits"
            }
            if (phoneText.length != 10) {
                return "Must be 10 Digits"
            }
            return null
        }

        fun checkValidPassword(textInput: TextInputEditText): String? {
            val passwordText = textInput.text.toString()
            if (!passwordText.matches(".*[0-9].*".toRegex())) {
                return "Must be all Digits"
            }
            if (passwordText.length != 4) {
                return "Must be 4 Digits"
            }
            return null
        }

        fun checkUserIdInputTextOnFocusListener(
            textInput: TextInputEditText,
            textLayout: TextInputLayout
        ) {
            textInput.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    textLayout.helperText = checkValidPhoneNumber(textInput)
                }
            }
        }

        fun checkPasswordInputTextOnFocusListener(
            textInput: TextInputEditText,
            textLayout: TextInputLayout
        ) {
            textInput.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    textLayout.helperText = checkValidPassword(textInput)
                }
            }
        }

        fun checkEmailInputTextOnFocusListener(
            textInput: TextInputEditText,
            textLayout: TextInputLayout
        ) {
            textInput.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    textLayout.helperText = checkValidEmailAddress(textInput)
                }
            }
        }
    }
}