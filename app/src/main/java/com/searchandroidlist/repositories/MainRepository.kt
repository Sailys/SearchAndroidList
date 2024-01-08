package com.searchandroidlist.repositories

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.moshi.Moshi
import com.searchandroidlist.apicalling.RetrofitApi
import com.searchandroidlist.apicalling.RetrofitService
import com.searchandroidlist.helpers.ErrorResponse
import com.searchandroidlist.helpers.ResultWrapper
import com.searchandroidlist.utils.UiHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class MainRepository (
    val retrofitApiService: RetrofitApi = RetrofitService.retrofitApiService(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        const val DEFAULT_PAGE_INDEX = 0
        const val DEFAULT_PAGE_SIZE = 10

        //get doggo repository instance
        fun getInstance() = MainRepository()
    }

    suspend fun getCountryList(): ResultWrapper<JsonElement> {
        return safeApiCall(dispatcher) { retrofitApiService.getCountryList() }
    }

    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        var errorResponse = ""
                        try {
                            val jsonObject: JSONObject = JSONObject(throwable.response()?.errorBody()?.string())
                            UiHelper.showLogs("Main Repo","" + jsonObject.optString("message"))
                            errorResponse = jsonObject.optString("message")
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                         ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}