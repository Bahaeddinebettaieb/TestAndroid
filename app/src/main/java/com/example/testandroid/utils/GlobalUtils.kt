package com.example.testandroid.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.location.Geocoder
import android.location.Location
import android.media.Image
import android.os.Build
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import androidx.camera.core.ImageProxy
import androidx.core.app.NotificationManagerCompat
import com.example.testandroid.R
import com.example.testandroid.domain.sealed.AuthenticationException
import com.example.testandroid.domain.sealed.GlobalResult
import com.example.testandroid.domain.sealed.NetworkErrorException
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class GlobalUtils {
    companion object {
        private val TAG = "GlobalUtils"


        fun resolveError(e: Exception): GlobalResult.ErrorState {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "connection error!")
                }
                is ConnectException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
            }

            if(e is HttpException){
                when(e.code()){
                    502 -> {
                        error = NetworkErrorException(e.code(),  "internal error!")
                    }
                    401 -> {
                        throw AuthenticationException("authentication error!")
                    }
                    400 -> {
                        error = NetworkErrorException.parseException(e)
                    }
                }
            }


            return GlobalResult.ErrorState(error)
        }

        fun navigateToActivityServiceWithExtraNoBack(
            activity: Context,
            start: Context,
            destination: Any,
            id: String?,
            name: String
        ) {
            activity.startActivity(
                Intent(start, destination as Class<Object>).putExtra("id", id)
                    .putExtra("name", name)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            )
        }

        fun navigateToActivityRightToLeft(
            activity: Activity,
            start: Context,
            destination: Any,
            id: String?,
            name: String
        ) {
            activity.startActivity(
                Intent(start, destination as Class<Object>).putExtra("id", id)
                    .putExtra("name", name).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        }

        fun navigateToActivityServiceWithExtra(
            activity: Context,
            start: Context,
            destination: Any,
            id: String?,
            name: String
        ) {
            activity.startActivity(
                Intent(start, destination as Class<Object>).putExtra("id", id)
                    .putExtra("name", name).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        fun navigateToActivity(
            activity: Activity,
            start: Context,
            destination: Any
        ) {
            activity.startActivity(Intent(start, destination as Class<Object>))
//            activity.overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit)
//            activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        }

        fun navigateToActivityWithExtra(
            activity: Activity,
            start: Context,
            destination: Any,
            id: String?,
            name: String
        ) {
            activity.startActivity(
                Intent(start, destination as Class<Object>).putExtra("id", id)
                    .putExtra("name", name).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            activity.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out)
        }

        fun navigateToActivityWithExtraWithAnimation(
            activity: Activity,
            start: Context,
            destination: Any,
            id: String?,
            name: String
        ) {
            activity.startActivity(
                Intent(start, destination as Class<Object>).putExtra("id", id)
                    .putExtra("name", name).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            activity.overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit)
        }

        fun myLocationChangedOrNot(
            latitude: Double,
            longitude: Double,
            latitude1: Double,
            longitude1: Double,
            arron: Int
        ): Boolean {
            var res = false
            val df = DecimalFormat()
            val df1 = DecimalFormat()
            df.maximumFractionDigits = arron //arrondi à 3 chiffres apres la virgules
            df.isDecimalSeparatorAlwaysShown = true
            df1.maximumFractionDigits = arron //arrondi à 3 chiffres apres la virgules
            df1.isDecimalSeparatorAlwaysShown = true
//            Log.e(TAG, "myLocationChanged: $latitude == $latitude1 <> $longitude == $longitude1")
            if ((df.format(latitude) != df.format(latitude1)) && (df.format(longitude) != df.format(
                    longitude1
                ))
            ) {
                res = true
            }
            return res
        }

        fun toDP(context: Context, value: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(), context.resources.displayMetrics
            ).toInt()
        }

        fun toDPFromFloat(context: Context, value: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(), context.resources.displayMetrics
            )
        }

        fun toSP(context: Context, value: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value.toFloat(), context.resources.displayMetrics
            ).toInt()
        }

        fun clearCountryCode(number: String): Boolean {
            var s: Boolean
            var s1 = number.substring(0, 1)
            var s2 = number.substring(0, 2)
            s = s2 == "00" || s1 == "+"
            return s
        }

        fun listIntContainsNumber(type: List<Int>, i: Int): Boolean {
            var have = false
            for (Int in type) {
                if (Int == i) {
                    have = true
                    Log.e(TAG, "listIntContainsNumber: $Int == $i")
                }
            }
            return have
        }

        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
                .matches()
        }

        fun changeTimestampToDate(timestamp: String?): String {
            Log.e(TAG, "changeTimestampToDate: $timestamp")
            var date = ""
            if (!timestamp.isNullOrEmpty()) {
                val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
                val netDate = Date(timestamp.toLong())
                date = sdf.format(netDate)
                Log.e(TAG, "Formatted Date= $date")
            }
            return date
        }

        fun convertFromBigDate(dateString: String?): Date? {
//            val dateStr=dateString?.substring(0,24)
            val sdf3 = SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z ('GMT'z)", Locale.ENGLISH)
            var d1: Date? = null
            try {
                d1 = sdf3.parse(dateString)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return d1
        }

        fun convertToFormatDate(dateString: String?): String {
            val originalFormat: java.text.DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val targetFormatDate: java.text.DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val targetFormatTime: java.text.DateFormat = SimpleDateFormat("hh:MM:ss")
            val date: Date = originalFormat.parse(dateString)
            return "${targetFormatDate.format(date)} a ${targetFormatTime.format(date)}"
        }

        fun convertToFormatDateWithoutSecond(dateString: String?): String {
            val originalFormat: java.text.DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            var targetFormatDate: java.text.DateFormat = SimpleDateFormat("dd/M/yyyy")
            var targetFormatTime: java.text.DateFormat = SimpleDateFormat("hh:MM")
            targetFormatTime.calendar.set(Calendar.SECOND, 0)
            val date: Date = originalFormat.parse(dateString)
//            var retour= "${targetFormatDate.format(date)} a ${targetFormatTime.format(date)}"
//            return retour.substring(0, retour.length - 4)
            return "${targetFormatDate.format(date)} a ${targetFormatTime.format(date)}"
        }

        fun getDate(time: Long): String? {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = time * 1000
            return DateFormat.format("dd-MM-yyyy", cal).toString()
        }

        fun convertToFormatDateWithDay(date: String?): String {
            val originalFormat: java.text.DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val targetFormat: java.text.DateFormat = SimpleDateFormat("EEE, dd MMMM")
            val date: Date = originalFormat.parse(date)
            return targetFormat.format(date)
        }


        fun convertToTime(date: String?): String {
            val originalFormat: java.text.DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val targetFormat: java.text.DateFormat = SimpleDateFormat("hh:mm a")
            val date: Date = originalFormat.parse(date)
            return targetFormat.format(date)
        }

        fun getAddress(lat: Double, lng: Double, context: Context): String {
            val geocoder = Geocoder(context)
            val list = geocoder.getFromLocation(lat, lng, 1)
            Log.e(TAG, "getAddress: " + list!![0].countryCode)
            return list[0].countryCode
        }

        fun getAddressComplete(lat: Double, lng: Double, context: Context): String {
            val geocoder = Geocoder(context, Locale.getDefault())
//            val geocoder = Geocoder(context)
            val list = geocoder.getFromLocation(lat, lng, 1)
//            Log.e(TAG, "getAddressComplete: " + list[0].getAddressLine(0))
            return list!![0].getAddressLine(0)
        }

        fun distanceBetweenTowLocation(
            latitude: Double,
            longitude: Double,
            latitude1: Double,
            longitude1: Double
        ): Float {
            val locationA = Location("point A")

            locationA.latitude = latitude
            locationA.longitude = longitude

            val locationB = Location("point B")

            locationB.latitude = latitude1
            locationB.longitude = longitude1
            return locationA.distanceTo(locationB)
        }

        fun removeNotification(context: Context, reqCode: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.getSystemService(NotificationManager::class.java)
                    .cancel(reqCode)
            } else {
                NotificationManagerCompat.from(context)
                    .cancel(reqCode)
            }
        }


        fun roundFloat(f: Float): Float {
            val c = (f + 0.5f)
            val n = f + 0.5f
            return if ((n - c) % 2 == 0f) f else c
        }

        fun toDecimalDigitsFloat(d: Float, decimalPlace: Int): Float {
            var bd = BigDecimal(java.lang.Float.toString(d))
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
            Log.e(TAG, "toDecimalDigitsFloat: $bd")
            return bd.toFloat()
        }

        fun convertToString(date: String): String {
            val c = Calendar.getInstance()
            val dateConverter = convertToDateString(date)
            val parts = dateConverter.split("-")
            var dateString = parts[2] + " " + convertMonthToString(parts[1].toInt())
            if (parts[0].toInt() == c.get(Calendar.YEAR))
                if (parts[1].toInt() == c.get(Calendar.MONTH) + 1)
                    if (parts[2].toInt() == c.get(Calendar.DAY_OF_MONTH)) {
                        dateString = "Today"
                    }
            return dateString
        }

        // TODO You need to change it to Date format and then you cast it to month name MMM
        fun convertMonthToString(month: Int): String {
            return when (month) {
                1 -> return "Janvier"
                2 -> return "Fevrier"
                3 -> return "Mars"
                4 -> return "Avril"
                5 -> return "Mai"
                6 -> return "Juin"
                7 -> return "Juillet"
                8 -> return "Aout"
                9 -> return "September"
                10 -> return "October"
                11 -> return "Novembre"
                else -> return "December"
            }
        }

        fun convertToDateString(date: String): String {
            var date2 = date.substring(0..9)
            val parts = date2.split("-")
            Log.i(
                TAG,
                "" + date2 + " ==>" + parts[0].toInt() + "/" + parts[1].toInt() + "/" + parts[2].toInt()
            )
            return date2
        }


        fun GetUnixTime(): Int {
            val calendar = Calendar.getInstance()
            val now = calendar.timeInMillis
            Log.e(TAG, "GetUnixTime: " + (now / 1000).toInt())
            return (now / 1000).toInt()
        }

        @SuppressLint("UnsafeOptInUsageError")
        fun imageToBitmap(imageProxy: ImageProxy): Bitmap {
            val image: Image? = imageProxy.image
            val yBuffer = image!!.planes[0].buffer // Y
            val vuBuffer = image!!.planes[2].buffer // VU

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val nv21 = ByteArray(ySize + vuSize)

            yBuffer.get(nv21, 0, ySize)
            vuBuffer.get(nv21, ySize, vuSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
            val imageBytes = out.toByteArray()

            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        fun toBitmap(image: Image): Bitmap? {
            val planes = image.planes
            val yBuffer: ByteBuffer = planes[0].buffer
            val uBuffer: ByteBuffer = planes[1].buffer
            val vBuffer: ByteBuffer = planes[2].buffer
            val ySize: Int = yBuffer.remaining()
            val uSize: Int = uBuffer.remaining()
            val vSize: Int = vBuffer.remaining()
            val nv21 = ByteArray(ySize + uSize + vSize)
            //U and V are swapped
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
            val imageBytes = out.toByteArray()
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        fun croppedNV21(mediaImage: Image, cropRect: Rect): ByteArray {
            val yBuffer = mediaImage.planes[0].buffer // Y
            val vuBuffer = mediaImage.planes[2].buffer // VU

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val nv21 = ByteArray(ySize + vuSize)

            yBuffer.get(nv21, 0, ySize)
            vuBuffer.get(nv21, ySize, vuSize)

            return cropByteArray(nv21, mediaImage.width, cropRect)
        }

        fun cropByteArray(array: ByteArray, imageWidth: Int, cropRect: Rect): ByteArray {
            val croppedArray = ByteArray(cropRect.width() * cropRect.height())
            var i = 0
            array.forEachIndexed { index, byte ->
                val x = index % imageWidth
                val y = index / imageWidth

                if (cropRect.left <= x && x < cropRect.right && cropRect.top <= y && y < cropRect.bottom) {
                    croppedArray[i] = byte
                    i++
                }
            }

            return croppedArray
        }

        fun imageProxyToYUV(imageProxy : ImageProxy): YuvImage {
            val yBuffer: ByteBuffer = imageProxy.planes[0].buffer
            val uBuffer: ByteBuffer = imageProxy.planes[1].buffer
            val vBuffer: ByteBuffer = imageProxy.planes[2].buffer

            // Full size Y channel and quarter size U+V channels.

            // Full size Y channel and quarter size U+V channels.
            val numPixels = (imageProxy.width * imageProxy.height * 1.5f).toInt()
            val nv21 = ByteArray(numPixels)

            var index = 0

            // Copy Y channel.
            val yRowStride: Int = imageProxy.planes[0].rowStride
            val yPixelStride: Int = imageProxy.planes[0].pixelStride
            for (y in 0 until imageProxy.height) {
                for (x in 0 until imageProxy.width) {
                    nv21[index++] = yBuffer[y * yRowStride + x * yPixelStride]
                }
            }

            // Copy VU data; NV21 format is expected to have YYYYVU packaging.
            // The U/V planes are guaranteed to have the same row stride and pixel stride.

            // Copy VU data; NV21 format is expected to have YYYYVU packaging.
            // The U/V planes are guaranteed to have the same row stride and pixel stride.
            val uvRowStride: Int = imageProxy.planes[1].rowStride
            val uvPixelStride: Int = imageProxy.planes[1].pixelStride
            val uvWidth: Int = imageProxy.width / 2
            val uvHeight: Int = imageProxy.height / 2

            for (y in 0 until uvHeight) {
                for (x in 0 until uvWidth) {
                    val bufferIndex = y * uvRowStride + x * uvPixelStride
                    // V channel.
                    nv21[index++] = vBuffer[bufferIndex]
                    // U channel.
                    nv21[index++] = uBuffer[bufferIndex]
                }
            }
            val uYvImg = YuvImage(
                nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height,  /* strides= */null
            )
            return uYvImg
        }

        fun rotateYUV420Degree90(
            data: ByteArray,
            imageWidth: Int,
            imageHeight: Int
        ): ByteArray? {
            val yuv = ByteArray(imageWidth * imageHeight * 3 / 2)
            // Rotate the Y luma
            var i = 0
            for (x in 0 until imageWidth) {
                for (y in imageHeight - 1 downTo 0) {
                    yuv[i] = data[y * imageWidth + x]
                    i++
                }
            }
            // Rotate the U and V color components
            i = imageWidth * imageHeight * 3 / 2 - 1
            var x = imageWidth - 1
            while (x > 0) {
                for (y in 0 until imageHeight / 2) {
                    yuv[i] = data[imageWidth * imageHeight + y * imageWidth + x]
                    i--
                    yuv[i] = data[imageWidth * imageHeight + y * imageWidth + (x - 1)]
                    i--
                }
                x = x - 2
            }
            return yuv
        }

    }
}