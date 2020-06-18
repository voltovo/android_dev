package suman.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

//장치에 센서를 사용하기 위해서 SensorManager 클래스 상속
 class MainActivity : AppCompatActivity(), SensorEventListener{

    //getSystemService 메소드로 SensorManager 객체 얻기
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE)as SensorManager
    }

    //TiltView 늦은 초기화
    private lateinit var tiltView: TiltView

    //센서 등록
    override fun onResume() {
        super.onResume()
        //registerListener() 사용할 센서를 등록
        //this를 지정하여 액티비티에서 센서값을 받도록 한다
        sensorManager.registerListener(this, 
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), //가속도 센서 지정 
                SensorManager.SENSOR_DELAY_NORMAL) //화면 방향이 전환될 때 적합한 정도로 센서값을 얻는다.
    }

    //센서 해제
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this) //센서 해제
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        //화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //가로 모드 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //this를 이용해서 TiltView를 초기화
        tiltView = TiltView(this)
        //R.layout.activity_main 대신에 tiltView를 setContentView() 메서드에 전달
        //tiltView 가 전체 레이아웃이 되었다.
        setContentView(tiltView)
    }

    //센서 정밀도가 변경되면 호출
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //센서값이 변경되면 호출
    override fun onSensorChanged(event: SensorEvent?) {
        //values[0] : x 축 값: 위로 기울이면 -10~0, 아래로 기울이면 0~10
        //values[1] : y 축 값: 왼쪽으로 기울이면 -10~0, 오른쪽으로 기울이면 0~10
        //values[2] : z 축 값: 미사용
        
        event?.let {
            //로그 표시(디버그용)
            Log.d("MainActivity","onSensorChanged: x :" +
            "${event.values[0]}, y :${event.values[1]}, z :${event.values[2]}")
        }
    }
}