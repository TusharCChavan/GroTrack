package com.clinicapp.models

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
class MDReferRequest (
        val clinic_id:Long
)