package suman.example.gpsmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.google.android.gms.maps.model.PolylineOptions
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //위치 정보를 주기적으로 얻는데 필요한 객체들 선언
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack
    private val REQUEST_ACCESS_FINE_LOCATION = 1000
    //PolyLine 옵션
    //선의 굵기, 색상 등을 설정
    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //세로 모드로 화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

    //실행 중 권한 요청 코드 적용
    override fun onResume() {
        super.onResume()

        //권한 요청
        permissionCheck(cancel = {
            //위치 정보가 필요한 이유 다이얼로그 표시
            showPermissionInfoDialog()
        }, ok = {
            //현재 위치를 주기적으로 요청 (권한이 필요한 부분)
            addLocationListener()
        })
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    //함수 인자 두개, 두 함수는 모두 인자가 없고, 반환값도 없다
    //이전에 사용자가 권환 요청을 거부한 적이 있다면 cancel() 호출
    //이전에 사용자가 권한 요청을 승낙한 적이 있다면 ok() 호출
    private fun permissionCheck(cancel:() -> Unit, ok:() -> Unit){
        //위치 권한이 있는지 검사
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //권한이 허용되지 않음
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)){
                //이전에 권한을 한 번 거부한 적이 있는 경우에 실행할 함수
                cancel()
            }else{
                //권한 요청
                ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION)
            }
        }else{
            //권한을 수락했을 때 실행할 함수
            ok()
        }
    }

    private fun showPermissionInfoDialog(){
        alert("현재 위치 정보를 얻으려면 위치 권한이 필요합니다.", "권한이 필요한 이유"){
            yesButton { 
                //권한 요청
                ActivityCompat.requestPermissions(this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION)
            }
            //no를 클릭하면 아무것도 하지 않고 다이얼로그가 닫힌다
            noButton {  }
        }.show()
    }

    override fun onPause() {
        super.onPause()
        //위치 요청을 취소
        removeLocationListener()
    }

    private fun removeLocationListener(){
        //현재 위치 요청을 삭제
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_ACCESS_FINE_LOCATION -> {
                if((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //권한 허용됨
                    addLocationListener()
                }else{
                    //권한 거부
                    toast("권한 거부 됨")
                }
                return
            }
        }
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

                Log.d("MapsActivity", "위도: $latitude, 경도: $longitude")

                //PolyLine에 좌표 추가
                polylineOptions.add(latLng)

                //선 그리기
                mMap.addPolyline(polylineOptions)
            }
        }
    }
}