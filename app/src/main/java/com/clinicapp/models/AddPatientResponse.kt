package com.clinicapp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.clinicapp.models.Patient

@JsonClass(generateAdapter = true)
class AddPatientResponse(
    @Json(name = "success")
    val status:Boolean,
    val message:String?,
    val client:Patient?,
    val errorType:String?,
    val error:Map<String,Array<String>>?

)
{

    fun showFieldError():Boolean{
        return "FIELD" == errorType;
    }

    fun getFirstNameError():String?{
        return getError("firstName")
    }

    fun getLastNameError():String?{
        return getError("lastName")
    }

    fun getPhoneError():String?{
        return getError("phone")
    }

     fun getDOBError():String?{
        return getError("dob")
    }

    private fun getError(key:String):String?{
        if(error == null) return "";
        val errors = error[key];
        if(errors != null && errors.isNotEmpty())
            return errors[0];
        return null;
    }


}
