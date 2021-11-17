package com.clinicapp.models

import android.os.Parcelable
import com.clinicapp.models.Client
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class ProductsResponse(
    @Json(name = "success")
    val status:Boolean,
    val products:List<Products>,
    val errorType:String?,
    val error:Map<String,Array<String>>?,
    val message:String?,

    ) : Parcelable {
}