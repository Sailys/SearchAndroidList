package com.searchandroidlist.helpers

data class ErrorResponse(
    val message: String, // this is the translated error shown to the user directly from the API
    val status: Int //this is for errors on specific field on a form
)
