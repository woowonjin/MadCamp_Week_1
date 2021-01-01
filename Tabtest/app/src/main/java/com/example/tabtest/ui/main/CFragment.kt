package com.example.tabtest.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tabtest.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
val base_time = "1800"
val base_data = "20210101"
val nx = "56"
val ny = "125"

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




class CFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_c, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        textView.text = "Frees"
        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_data, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER>{
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful){
                    Log.d("api", response.body().toString())
                    val tmp: LatXLngY? = convertGRID_GPS(TO_GRID, 36.3741555, 127.3658293)
                    Log.d("grid", tmp!!.x.toString())
                    Log.d("grid", tmp!!.y.toString())
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
}
