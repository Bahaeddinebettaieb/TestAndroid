package com.example.testandroid.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        fun convertDateFromLongToString(time: Long?): String {
            return SimpleDateFormat("dd-MM-yyyy - HH:mm").format(Date(time!!))
        }
        fun convertDateFromLongToString2(time: Long?): String {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date(time!!))
        }

        fun convertDateFromLongToStringWithoutTime(time: Long?): String {
            //return SimpleDateFormat("dd/MM/yyyy").format(Date(time!!))
            return SimpleDateFormat("yyyy/MM/dd").format(Date(time!!))
        }

        fun getDateToFromat(reciveDate: String?): String? {
            val fromServer = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val myFormat = SimpleDateFormat("dd/MM/yy HH:mm")
            return myFormat.format(fromServer.parse(reciveDate))
        }
    }
}