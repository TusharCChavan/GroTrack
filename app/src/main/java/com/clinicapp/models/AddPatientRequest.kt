package com.clinicapp.models

import android.text.TextUtils
import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat

@JsonClass(generateAdapter = true)
class AddPatientRequest(
    val first_name:String?,
    val last_name:String?,
    val phone:String?,
    val gender:String?,
    val ethnicity:String?,
    val email:String?,
 val dob:String?
)
{

    fun toPatient(id:Long):Patient{
        return Patient(id, first_name,last_name,"","","",gender, phone,"");
    }

    companion object {
        @JvmStatic
        fun validateFirstName(first_name:String):Pair<Boolean,String?> {
            return isEmptyWithError(first_name, "First name");
        }

        @JvmStatic
        fun validateLastName(last_name:String):Pair<Boolean,String?> {
            return isEmptyWithError(last_name, "Last name");
        }

        @JvmStatic
        fun validateDateOfBirth(dob:String):Pair<Boolean,String?> {
            try {
                val format = SimpleDateFormat("Y-m-d");
                format.parse(dob)
            } catch (e:Exception){
                return Pair(false, "Invalid date.")
            }
            return isEmptyWithError(dob, "Date of birth");
        }

        @JvmStatic
        fun validateGender(dob:String):Pair<Boolean,String?> {
            return isEmptyWithError(dob, "Gender ");
        }

        @JvmStatic
        fun validatePhone(phone:String):Pair<Boolean,String?> {
            return isEmptyWithError(phone, "Phone number ");
        }

        private fun isEmptyWithError(value:String?, fieldName:String):Pair<Boolean,String?>{
            if(TextUtils.isEmpty(value)) {
                return Pair(false, "$fieldName is required.");
            }
            return Pair(true,null);
        }

    }
}
