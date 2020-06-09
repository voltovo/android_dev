package junsuk.example.bimcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.toast

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //전달받은 키,몸무게
        val height = intent.getStringExtra("height").toInt()
        val weight = intent.getStringExtra("weight").toInt()

        //BMI 계산
        val bmi = weight / Math.pow(height / 100.0, 2.0)
        
        //결과 표시
        when{
            bmi >= 35 -> resultTextView.text = "고도 비만"
            bmi >= 30 -> resultTextView.text = "2단계 비만"
            bmi >= 25 -> resultTextView.text = "1단계 비만"
            bmi >= 23 -> resultTextView.text = "과체중"
            bmi >= 18.5 -> resultTextView.text = "정상"
            else -> resultTextView.text = "저체중"
        }

        //이미지 표시
        when{
            bmi >= 23 ->
                imageView.setImageResource(
                    R.drawable.ic_baseline_sentiment_very_dissatisfied_24
                )
            bmi >= 18.5 ->
                imageView.setImageResource(
                    R.drawable.ic_baseline_child_care_24
                )
            else ->
                imageView.setImageResource(
                    R.drawable.ic_baseline_mood_bad_24
                )
        }
        //토스트 메세지 적용 (잠깐 보였다가 사라지는 메세지
        Toast.makeText(this, "$bmi", Toast.LENGTH_SHORT).show()

        //Anko 적용 토스트 메세지
        toast("$bmi")
    }
}