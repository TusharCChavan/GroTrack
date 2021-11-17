package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)

class Client(
    val firstName:String?,
    val lastName:String?,
) : Parcelable {
}