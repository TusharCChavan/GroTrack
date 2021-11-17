package com.clinicapp.models

import android.os.Parcelable
import com.clinicapp.models.Client
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class BuyProductResponse(
    @Json(name = "success")
    val status:Boolean,
    val errorType:String?,
    val error:Map<String,Array<String>>?,
    val message:String?,

    ) : Parcelable {
}