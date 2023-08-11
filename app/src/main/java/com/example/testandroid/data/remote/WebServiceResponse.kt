package com.example.testandroid.data.remote

import com.google.gson.annotations.SerializedName

data class WebServiceResponse(
    @SerializedName("status")
    var status: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var data: Any?
) {
    override fun toString(): String {
        return "WebServiceResponse(status=$status, message='$message', data=$data)"
    }
}