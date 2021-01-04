package com.example.tabtest

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
=======
import android.view.MotionEvent
>>>>>>> feature#13
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tabtest.ui.main.FragmentLifecycle
import com.example.tabtest.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import java.lang.Exception

class MainActivity : AppCompatActivity(), LocationListener {
    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var Latitude = String()
    var Longtitude = String()
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    var location : Location? = null
    var mGeocoder : Geocoder? = null
    var state = String()
    var city = String()

    ///GESTURE
    private val OnTouchListener= ArrayList<MyOnTouchListener>()

    public interface MyOnTouchListener{
        fun OnTouch(ev: MotionEvent?)
    }

    fun registerMyOnTouchListener(listener: MyOnTouchListener){
        OnTouchListener.add(listener)
        println("ADD")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in OnTouchListener) listener.OnTouch(ev)
        return super.dispatchTouchEvent(ev)
    }


    private val pageChangeListener: OnPageChangeListener= object : OnPageChangeListener {
        var currentPosition = 0
        override fun onPageSelected(newPosition: Int) {
            val fragmentToHide: FragmentLifecycle = sectionsPagerAdapter.getItem(currentPosition) as FragmentLifecycle
            fragmentToHide.onPauseFragment()
            val fragmentToShow: FragmentLifecycle = sectionsPagerAdapter.getItem(newPosition) as FragmentLifecycle
            fragmentToShow.onResumeFragment()
            currentPosition = newPosition
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGeocoder = Geocoder(this)
        getLocation()
        setContentView(R.layout.activity_main) // activity main view 확인
        //val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter // viewpager adapter 설정
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager) // pager와 tab layout 연결

        viewPager.setOnPageChangeListener(pageChangeListener)



    }




//    override fun onResume(){
//        super.onResume()
//        getLocation()
//    }

    public fun getGeo(){
        try{
            println("Latitude is $Latitude")
            println("lognitude is $Longtitude")
            val resultList = mGeocoder?.getFromLocation(Latitude.toDouble(), Longtitude.toDouble(), 5)
            Log.d("getGeo Complete", "${resultList?.get(0)?.getAddressLine(0)}")
            val resultAddress = resultList?.get(0)?.getAddressLine(0).toString().split(", ")
            val country = resultAddress[resultAddress.size-1]
            state = resultAddress[resultAddress.size-2]
            city = resultAddress[resultAddress.size-3]
        }catch (e : Exception){
            Log.d("Error", "주소 변환 실패")
        }
    }

    public fun getLocation() {
        println("Get Location")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        else { //Permission Granted
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location == null){
                Log.d("Error", "LastKnownLocation is null")
            }
            else {
                Latitude = location?.latitude.toString()
                Longtitude = location?.longitude.toString()
                getGeo()
            }
            if (isGPSEnabled) {
                println("Gps is Enabled")
                if(locationManager != null) {
                    try {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            10f,
                            this
                        )
                        Log.d("Success", "RequestLocationUpdates Success")
                    }
                    catch (e : Exception){
                        Log.d("Error", "Cannot RequestLocationUpdates")
                    }
                }
                else{
                    println("Error : Location Manager is null")
                }
            }
            else {
                println("Error : Please turn on the GPS")
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        println("onLocationChaged !!")
        Latitude = location.latitude.toString()
        Longtitude = location.longitude.toString()
        getGeo()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun getLat() : String{
        return Latitude
    }

    public fun getLng() : String{
        return Longtitude
    }

    fun getAddress() : String{
        return "$state $city"
    }
}