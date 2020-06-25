package suman.example.gpsmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //위치 정보를 주기적으로 얻는데 필요한 객체들 선언
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
    }

    //위치 정보를 얻기 위한 각종 초기화
    
    private fun locationInit(){
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest()

        //GPS 우선
        //PRIORITY_HIGH_ACCURACY = 가장 정확한 위치를 요청
        //PRIORITY_BALANCED_POWER_ACCURACY = 블록 수준의 정확도를 요청
        //PRIORITY_LOW_POWER = 도시 수준의 정확도를 요청
        //PRIORITY_NO_POWER = 추가 전력 소모 없이 최상의 정확도를 요청
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //업데이트 인터벌
        //위치 정보가 없을 때는 업데이트 안함
        //상황에 따라 짧아질 수 있음, 정확하지 않음
        //다른 앱에서 짧은 인터벌로 위치 정보를 요청하면 짧아질 수 있음
        locationRequest.interval = 10000
        //정확함. 이것보다 짧은 업데이트는 하지 않음
        //fastestInterval = 다른 앱에서 위치를 갱신했을 때 그 정보를 가장 빠른 간격으로 입력함
        locationRequest.fastestInterval = 5000
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onResume() {
        super.onResume()
        addLocationListener()
    }

    private fun addLocationListener(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    //LocationResult 객체를 반환하고 lastLocation 프로퍼티로 Location 객체를 얻는다
    inner class MyLocationCallBack : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation
            //기기의 GPS 설정이 꺼져 있거나 현재 위치 정보를 얻을 수 없는 경우에 Location 객체가 null 일 수 있다.
            //null 이 아닐 경우 해당 위도와 경도 위치로 카메라를 이동
            location?.run{
                //14 level로 확대하고 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            }
        }
    }
}