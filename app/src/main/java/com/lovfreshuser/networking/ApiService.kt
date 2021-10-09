package com.lovfreshuser.networking

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        val ROOT_URL: String = "https://relax.spinnertechltd.com/api/"
        val IMAGE_URL: String = "https://relax.spinnertechltd.com/storage/"
    }


}