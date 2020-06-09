package junsuk.example.bimcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //이전에 입력한 값 읽어오기
        loadData()

        resultButton.setOnClickListener{
            /*//안드로이드 액티비를 전환할 때마다 항상 인텐트 객체를 생성한다.
            //두번째 화면으로 이동
            val intent = Intent(this, ResultActivity::class.java)
            //startActivity() 호출
            startActivity(intent)*/

            //코드의 양을 줄이기 위해서 Anko 라이브러리 사용
            //두번째 화면으로 이동
            //startActivity<ResultActivity>()

            //인텐트에 데이터를 담기
            /*val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("weight", weightEditText.text.toString())
            intent.putExtra("height", heightEditText.text.toString())
            startActivity(intent)*/

            //마지막에 입력한 내용 저장
            saveData(heightEditText.text.toString().toInt(),
                    weightEditText.text.toString().toInt())
            //Anko 라이브러리 사용
            startActivity<ResultActivity>(
                "weight" to weightEditText.text.toString(),
                "height" to heightEditText.text.toString()
            )
        }

    }

    //데이터 저장하는 메소드
    private fun saveData(height:Int, weight:Int){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        editor.putInt("KEY_HEIGHT", height)
            .putInt("KEY_WEIGHT", weight)
            .apply()
    }

    //데이터 불러오기 메소드
    private fun loadData(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val height = pref.getInt("KEY_HEIGHT",0)
        val weight = pref.getInt("KEY_WEIGHT",0)

        if(height != 0 && weight !=0){
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}