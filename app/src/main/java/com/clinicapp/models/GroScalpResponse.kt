package com.clinicapp.models

import android.os.Parcelable
import com.clinicapp.models.Client
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class GroScalpResponse(
    @Json(name = "success")
    val status:Boolean,
    val standard:List<ScalpStandard>,
    val user_data:List<GroScalp_UserData>,
    val errorType:String?,
    val error:Map<String,Array<String>>?,
    val message:String?,

    ) : Parcelable {
}