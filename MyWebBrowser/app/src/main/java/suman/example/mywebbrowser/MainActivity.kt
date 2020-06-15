package suman.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹뷰 기본 설정
        webView.apply {
            //자바 스크립트 기능을 켜야 작동한다.
            settings.javaScriptEnabled = true
            //웹뷰에 페이지가 표시된다.
            //WebViewClient 클래스를 지정하지 않으면 자체 웹브라우저가 동작한다.
            //두 가지 설정은 웹뷰를 사용할 때 꼭 해야 한다.
            webViewClient = WebViewClient()
        }
        //웹뷰에 해당 페이지 로딩
        webView.loadUrl("http://www.google.com")

        //에디트텍스트가 선택되고 글자가 입력될 때마다 호출
        //인자값 (반응하는 뷰, 액션ID, 이벤트)
        //여기서는 액션ID만 사용하기 떄문에, _ 로 대치
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            //검색 버튼을 눌렀는지 체크
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                //검색 창에 입력한 주소를 웹뷰에 전달하여 로딩
                webView.loadUrl(urlEditText.text.toString())
                true
            }else{
                false
            }
        }

        //컨텍스트 메뉴 등록
        registerForContextMenu(webView)
    }
    
    override fun onBackPressed() {
        //웹뷰가 이전 페이지로 갈 수 있다면
        if(webView.canGoBack()){
            //이전 페이지로 이동
            webView.goBack()
        }else{
            //그렇지 않다면 원래 동작 수행
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //메뉴 리소스를 엑티비티의 메뉴로 표시 하기 위해서 inflate() 메서드를 사용ㅇ
        menuInflater.inflate(R.menu.main, menu)
        //true를 반환하면 액티비티에 메뉴가 있다고 인식
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //메뉴 아이템으로 분기를 수행
        when(item?.itemId){
            //구글,집 아이콘 클릭 시 구글 페이지로 이동
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("http://www.google.com")
                return true
            }
            //네이버 클릭 시 이동
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
                return true
            }
            //다음 클릭 시 이동
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
                return true
            }
            //연락처 클릭하면 전화 앱을 실행
            //암시적 인텐트
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
                return true
            }
            
            R.id.action_send_text -> {
                //문자 보내기
                return true
            }

            R.id.action_email -> {
                //이메일 보내기
                return true
            }
        }
        //의도한 상황을 제외한 나머지 상황에서는 super 메서드를 호출하는것이 
        //안드로이드 시스템에서의 보편적인 규칙
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //메뉴 리소스를 액티비티의 컨텍스트 메뉴로 사용
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_share -> {
                //페이지 공유
                return true
            }

            R.id.action_browser -> {
                //기본 웹 브라우저에서 열기
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}