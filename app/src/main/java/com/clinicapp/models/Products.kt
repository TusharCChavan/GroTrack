package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class Products(
    val id:Long,
    val name:String,
    val description:String,
    val image:String,
    val created_at:String,

) : Parcelable {
}