package com.clinicapp.models

import android.os.Parcelable
import com.clinicapp.models.Client
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class GroScalp_UserDataImg(

    val id:Long,
    val created_at:Long,
    val img: String?,
    val subType:String

    ) : Parcelable {
}