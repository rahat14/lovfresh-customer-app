package com.lovfreshuser.models.notification


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    var email: String? = "", // testh158@gmail.com
    @SerializedName("first_name")
    var firstName: String? = "", // hello
    @SerializedName("id")
    var id: Int? = 0, // 152
    @SerializedName("is_superuser")
    var isSuperuser: Boolean? = false, // false
    @SerializedName("phone_number")
    var phoneNumber: String? = "" // +8801712810876
)