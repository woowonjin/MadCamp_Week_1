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
import com.google.gson.JsonObject
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
import kotlin.reflect.typeOf

var lat : String = ""
var lng : String = ""
val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
var base_time = "1800"
var base_data = "20210101"
var nx = "56"
var ny = "125"

var tmp: String = ""
var rain: String = ""
var wind: String = ""
var humi: String = ""
var winddriec: String = ""
var rainform: String = ""



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
        val baseDate : String,
        val baseTime : String,
        val category : String,
        val nx : String,
        val ny : String,
        val obsrValue: String
)

interface WeatherInterface {
    @GET("getUltraSrtNcst?serviceKey=zJI20aUR59nMTl5ApsYTsvuvZAWgThqp745YEiuqUfReu9PD9M7PDvC12UF1ha8uZPNwV75JuwB4mfhUSKOL9A%3D%3D")
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




class CFragment : Fragment(), FragmentLifecycle {
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd,HHmm")
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c, container, false)
//        val act = activity as MainActivity
//        act.getLocation()
//        getCurrentDate()
//        getCurrentPosition()
//        val grid: LatXLngY? = convertGRID_GPS(TO_GRID, lat as Double, lng as Double)
//        nx = grid!!.x.toString()
//        ny = grid!!.y.toString()
//        Log.d("grid", lat)

//        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_data, base_time, nx, ny)
//        call.enqueue(object : retrofit2.Callback<WEATHER>{
//            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
//                if (response.isSuccessful){
//                    Log.d("api", response.body().toString())
//                    val tmp: LatXLngY? = convertGRID_GPS(TO_GRID, 36.3741555, 127.3658293)
//                    Log.d("grid", tmp!!.x.toString())
//                    Log.d("grid", tmp!!.y.toString())
////                    Log.d("api", response.body()!!.response.body.items.item.toString())
////                    Log.d("api", response.body()!!.response.body.items.item[0].category)
//
//                }
//            }
//            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
//                //Log.d("api fail : ", t.message)
//            }
//        })

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


    override fun onPauseFragment() {
        Log.d("tab","pauseC")
        this.onDestroyView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResumeFragment() {
        Log.d("tab","resumeC")
        getCurrentDate()
        getCurrentPosition()
        println("date is $base_data")
        println("time is $base_time")

        var grid: LatXLngY?

        if (!(lat == "") && !(lng == "")) {
            grid = convertGRID_GPS(TO_GRID, lat.toDouble(), lng.toDouble())
        }
        else{
            grid = null
            Log.d("error","retry load lat and lng")
        }
        nx = grid?.x?.toInt().toString()
        ny = grid?.y?.toInt().toString()
        println("$nx, $ny")
        fun callweather() {
            val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_data, base_time, nx, ny)
            call.enqueue(object : retrofit2.Callback<WEATHER> {
                override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                    if (response.isSuccessful) {
                        println("$base_data, $base_time")
                        Log.d("api", "${response.body()}")
                        if (response.body()?.response?.header?.resultCode == 0) {
                            println("loading weather success")
                            for (item in response!!.body()!!.response.body?.items?.item) {
                                when (item.category) {
                                    "T1H" -> {
                                        tmp = item.obsrValue
                                    }
                                    "RN1" -> {
                                        rain = item.obsrValue
                                    }
                                    "REH" -> {
                                        humi = item.obsrValue
                                    }
                                    "PTY" -> {
                                        rainform = item.obsrValue
                                    }
                                    "VEC" -> {
                                        winddriec = item.obsrValue
                                    }
                                    "WSD" -> {
                                        wind = item.obsrValue
                                    }
                                    else -> println("NO NEED")
                                }

                            }

                        }
                        else if (response.body()?.response?.header?.resultCode == 3) {
                            if (base_time.toInt() >= 100) {
                                if ((base_time.toInt() - 100) < 1000) {
                                    base_time = "0" + (base_time.toInt() - 100).toString()
                                } else {
                                    base_time = (base_time.toInt() - 100).toString()
                                }
                            } else {
                                base_time = "2300"
                            }
                            callweather()

                        } else {
                            println("FAIL LOAD WEATHER")
                        }
                        //                    val tmp: LatXLngY? = convertGRID_GPS(TO_GRID, 36.3741555, 127.3658293)
                        //                    Log.d("grid", tmp!!.x.toString())
                        //                    Log.d("grid", tmp!!.y.toString())
                        //                    Log.d("api", response.body()!!.response.body.items.item.toString())
                        //                    Log.d("api", response.body()!!.response.body.items.item[0].category)
                        println("온도:$tmp, 강수량:$tmp, 습도:$humi, 강수형태:$rainform, 풍향:$winddriec, 풍속:$wind")
                    }
                }

                override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                    println("FAIL LOAD WEATHER")
                }
            })
        }

        callweather()

    }

}
