package com.example.testandroid.data.remote

class ApiEndPoints {
    companion object {
        const val BASE_URL = "https://takafol.itgate-group.com/api/"
        //        const val BASE_URL = "http://192.168.1.242:3004/api/"
        const val BASE_URL_SOCKET = "http://192.168.1.127:8001/websocket"
        const val BASE_URL_IMAGE = "https://takafol.itgate-group.com/getimage/"
//        const val BASE_URL_IMAGE = "http://192.168.1.226:3004/api/getimage/"
        const val BASE_URL_VIDEO= "https://takafol.itgate-group.com/getimage/"
        const val BASE_URL_FILE= "https://takafol.itgate-group.com/getfile/"

        const val LOGIN = "users/signin/{lang}"

        //Cities
        const val LIST_CITIES = "cities/{lang}"
        const val LIST_ADS = "ads/{lang}"

    }
}