package junsuk.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    //시간을 계산할 변수 초기화
    private var time = 0
    //Timer를 취소할려면 실행되고 반환되는 Timer 객체를 저장할 변수가 필요
    //null을 허용하는 Timer 타입으로 선언
    private var TimerTask: Timer? =null
    private var isRunning = false
    //lap 변수를 1로 초기화
    private var lap = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //시작 또는 일시 정지
        fab.setOnClickListener{
            //타이머 동작중 여부를 판단하는 변수 반전시키고
            isRunning = !isRunning
            
            //실행중이면 일시정지, 정지중이면 시작
            if(isRunning){
                start()
            }else{
                pause()
            }
        }

        //렙타입 기록
        lapButton.setOnClickListener{
            recordLapTime()
        }

        //타임 리셋
        resetFab.setOnClickListener{
            reset()
        }

    }

    //랩타입 기록
    private fun recordLapTime(){
        //현재 시간을 지역 변수에 저장
        val lapTime = this.time
        //동적으로 TextView를 생성해서 시간을 계산한 후 문자열로 설정한다
        val textView = TextView(this)

        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"

        //맨 위에 랩타입 추가
        lapLayout.addView(textView, 0)
        lap++
    }


    //stopWatch start
    private fun start(){
        //Fab 을 누르면 시작하고 일시정지 이미지로 변환한다
        fab.setImageResource(R.drawable.ic_pause_24)

        TimerTask = timer(period = 10){
            //0.01초마다 이 변수를 증가 시켜서 UI 갱신
            time++
            val sec = time / 100
            val milli = time % 100
            //runOniThread을 사용해서 UI 조작
            runOnUiThread {
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }
    
    //stopWatch pause
    private fun pause(){
        //fab을 클릭하면 다시 시작 이미지로 교체
        fab.setImageResource(R.drawable.ic_play_arrow_24)
        //null 이 아닌 경우만 호출
        //실행중인 타이머가 있다면 취소
        TimerTask?.cancel()
    }

    //time reset
    private fun reset(){
        //null이 아닌 경우만 호출
        //실행 중인 타이머가 있다면 취소
        TimerTask?.cancel()

        //모든 변수 초기화
        time = 0
        isRunning = false
        fab.setImageResource(R.drawable.ic_play_arrow_24)
        secTextView.text = "0"
        milliTextView.text = "00"

        //모든 랩타입 제거
        lapLayout.removeAllViews()
        lap = 1
    }
}