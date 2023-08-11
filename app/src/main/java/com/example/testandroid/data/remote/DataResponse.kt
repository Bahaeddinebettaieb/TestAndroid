package com.example.testandroid.data.remote

import com.google.gson.annotations.SerializedName

class DataResponse<T> (
    @SerializedName("status")
    var status: Int?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var data: T?
){
    override fun toString(): String {
        return "DataResponse(status=$status, message=$message, data=$data)"
    }
}