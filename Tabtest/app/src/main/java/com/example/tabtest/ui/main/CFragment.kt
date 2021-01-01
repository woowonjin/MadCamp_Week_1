package com.example.tabtest.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tabtest.MainActivity
import com.example.tabtest.R
import com.google.android.gms.maps.model.LatLng
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

var lat : String = ""
var lng : String = ""
val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
var base_time = "1110"
var base_data = "20210101"
var nx = "55"
var ny = "127"

data class WEATHER (
        val response : RESPONSE
)
data class RESPONSE (
        val header : HEADER,
        val body : BODY
)
data class HEADER(
        val resultCode : Int,
        val resultMsg : String
)
data class BODY(
        val dataType : String,
        val items : ITEMS
)
data class ITEMS(
        val item : List<ITEM>
)
data class ITEM(
        val baseDate : Int,
        val baseTime : Int,
        val category : String,
        val fcstDate : Int,
        val fcstTime : Int,
        val fcstValue: String
)

interface WeatherInterface {
    @GET("getVilageFcst?serviceKey=zJI20aUR59nMTl5ApsYTsvuvZAWgThqp745YEiuqUfReu9PD9M7PDvC12UF1ha8uZPNwV75JuwB4mfhUSKOL9A%3D%3D")
    fun GetWeather(
            @Query("dataType") data_type : String,
            @Query("numOfRows") num_of_rows : Int,
            @Query("pageNo") page_no : Int,
            @Query("base_date") base_date : String,
            @Query("base_time") base_time : String,
            @Query("nx") nx : String,
            @Query("ny") ny : String
    ): Call<WEATHER>
}


private val retrofit = Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/") // 마지막 / 반드시 들어가야 함
        .addConverterFactory(GsonConverterFactory.create()) // converter 지정
        .build() // retrofit 객체 생성

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}




class CFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd,HHmm")

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c, container, false)
        getCurrentDate()
        getCurrentPosition()
        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_data, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER>{
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful){
                    Log.d("api", response.body().toString())
//                    Log.d("api", response.body()!!.response.body.items.item.toString())
//                    Log.d("api", response.body()!!.response.body.items.item[0].category)
                }
            }
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                //Log.d("api fail : ", t.message)
            }
        })
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(){
        var dateAndtime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        var current = dateAndtime.format(formatter)
        current = current.toString()
        val result = current.split(",")
        val currentDate = result[0]
        val currentTime = result[1]
        base_data = currentDate
        base_time = currentTime
    }

    fun getCurrentPosition(){
        val act = activity as MainActivity
        lat = act.getLat()
        lng = act.getLng()
        println("Latitude : $lat")
        println("Longtitude : $lng")
    }

}
