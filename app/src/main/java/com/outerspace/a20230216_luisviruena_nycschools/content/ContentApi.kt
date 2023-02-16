package com.outerspace.a20230216_luisviruena_nycschools.content

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://data.cityofnewyork.us/"
private const val API_KEY_ID = "eo4fr9ovq82ex5a5o1mbazb2w"
private const val API_KEY_SECRET = "3yw5h1bqj9p8v377s77tldrwjcta3aneevj2ggcpk5dc16n14f"
private const val APP_TOKEN = "uiN5nBJu9ZnJGN8xIpREF55k1"

// NOTE: This is the endpoint given in the exercise. When I open it, it gets redirected
// to https://data.cityofnewyori.us/resource/...
// therefore, instead of using Education/DOE-High-School-Directory-2017 I am using the
// redirected one.

// private const val DOE_END_POINT = "Education/DOE-High-School-Directory-2017/"
private const val DOE_END_POINT = "resource/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ContentApiService {
    @GET("${DOE_END_POINT}s3k6-pzi2.json")
    suspend fun getMainListing(
        @Query("\$\$app_token") appToken: String,
        @Query("\$select") selectFields: String,
    ) : List<SchoolShort>

    @GET("${DOE_END_POINT}s3k6-pzi2.json")
    suspend fun getSchoolsByDbn(
        @Query("\$\$app_token") appToken: String,
        @Query("dbn") dbn: String,
    ) : List<School>

    @GET("${DOE_END_POINT}f9bf-2cp4.json")
    suspend fun getSatByDbn(
        @Query("\$\$app_token") appToken: String,
        @Query("dbn") dbn: String,
    ) : List<SatScores>
}

object ContentApi {
    private val retrofitService: ContentApiService by lazy {
        retrofit.create(ContentApiService::class.java)
    }

    suspend fun getMainListing(): List<SchoolShort> {
        return retrofitService.getMainListing(APP_TOKEN, "dbn,school_name,primary_address_line_1,city,state_code,zip")
    }

    suspend fun getSchools(dbn: String): List<School> {
        return retrofitService.getSchoolsByDbn(APP_TOKEN, dbn)
    }

    suspend fun getSatScores(dbn: String): List<SatScores> {
        return retrofitService.getSatByDbn(APP_TOKEN, dbn)
    }
}
