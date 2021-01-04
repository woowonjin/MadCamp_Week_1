package com.example.tabtest

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tabtest.ui.main.FragmentLifecycle
import com.example.tabtest.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity(), LocationListener {
    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var Latitude = String()
    var Longtitude = String()
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

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

    public fun getLocation() {
        println("Get Location")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        Latitude = location.latitude.toString()
        Longtitude = location.longitude.toString()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
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
}