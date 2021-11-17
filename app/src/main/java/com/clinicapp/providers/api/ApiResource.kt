package com.clinicapp.providers.api

import com.clinicapp.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiResource {
    @POST("salon/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("client/create")
    fun addPatient(@Body request: AddPatientRequest): Call<AddPatientResponse>

    @POST("client/search")
    fun searchPatient(@Body request: AddPatientRequest): Call<SearchPatientsResponse>

    @POST("client/{client_id}/hair_analysis/create")
    fun createHairAnalysis(@Body request: HairAnalysisRequest,@Path( "client_id") client_id:Long): Call<HairAnalysisResponse>

    @Multipart
    @POST("client/{client_id}/hair_analysis/{hair_analysis_id}/upload/image")
    fun uploadImage(@Part mediaTye:MultipartBody.Part, @Part subType:MultipartBody.Part,
                    @Part image:MultipartBody.Part,@Path( "hair_analysis_id") hair_analysis_id:Long
                    ,@Path( "client_id") client_id:Long): Call<UploadImageResponse>


    @POST("client/{client_id}/hair_analysis/all/")
    fun getGroScale(@Path( "client_id") client_id:Long): Call<GroScaleResponse>

   @GET("products")
    fun getProducts(): Call<ProductsResponse>

  @GET("clinics/all")
    fun get_md(): Call<MDResponse>


    @POST("client/{client_id}/product/{product_id}/buy")
    fun buy_product(@Path( "product_id") product_id:Long
                    ,@Path( "client_id") client_id:Long): Call<BuyProductResponse>

  @POST("client/{client_id}/send_to_clinic")
    fun refer_to_md(@Path( "client_id") client_id:Long,@Body clinic_id: MDReferRequest): Call<MD_ReferResponse>

 @GET("client/{client_id}/gro_scalp")
    fun get_groscalp_info(@Path( "client_id") client_id:Long): Call<GroScalpResponse>

 @POST("client/{client_id}/gro_scalp/{scalp_id}")
    fun get_groscalp_match(@Path( "client_id") client_id:Long
                           ,@Path( "scalp_id") scalp_id:Long,): Call<GroScalpMatchResponse>


}