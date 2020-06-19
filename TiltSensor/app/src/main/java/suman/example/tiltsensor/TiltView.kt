package suman.example.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View

class TiltView(context: Context?) : View(context) {

    private val greenPaint:Paint = Paint()
    private val blackPaint:Paint = Paint()
    private var cX: Float = 0f
    private var cY: Float = 0f
    private var xCoord: Float = 0f
    private var yCoord: Float = 0f

    init {
        //녹색 페인트
        greenPaint.color = Color.GREEN
        
        //검은색 테두리 페인트 (STROKE = 외각선만 그립니다.)
        blackPaint.style = Paint.Style.STROKE
    }

    //뷰틔 크기가 변경될 떄 호출
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //뷰의 크기가 변경될 때마다... 중앙을 알기 위해서 재정의
        cX = w / 2f
        cY = h / 2f
    }
    
    override fun onDraw(canvas: Canvas?) {
        //바깥 테두리
        canvas?.drawCircle(cX,cY,100f,blackPaint)
        //녹색 원
        //녹색원을 그릴때마다 Coord값을 더해줌
        //센서 값이 변경될 때마다 onSensorEvent() 메서드를 호출한다.
        canvas?.drawCircle(xCoord + cX,yCoord + cY,100f,greenPaint)
        //가운데 십자가
        canvas?.drawLine(cX - 20, cY, cX + 20, cY, blackPaint)
        canvas?.drawLine(cX, cY - 20, cX, cY + 20, blackPaint)
        
    }

    //센서값 받기
    fun onSensorEvent(event: SensorEvent){
        //화면을 가로로 돌렸으므로 X축과 Y축을 서로 바꿈
        yCoord = event.values[0] * 20
        xCoord = event.values[1] * 20
        //onDDraw() 다시 호출하는 메서드
        //즉 뷰를 다시 그린다는 의미
        invalidate()
    }
}