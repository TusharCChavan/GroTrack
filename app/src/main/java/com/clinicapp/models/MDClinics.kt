package com.clinicapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
class MDClinics(
    val clinic_id:Long?,
    val name:String?,
    val email:String?,
    val admin_profile:Admin_Profile?

) : Parcelable {
}