package suman.example.mygallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_photo.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//const를 사용하여 시간에 결정되는 상수 선언
//파일 내 어디서든지 사용 가능
private const val ARG_URI = "uri"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //onCreateView()로 프래그먼테 표시될 뷰 생성
        //액티비티가 아닌곳에서 레이아웃 리소스를 가져오기 위해서 inflate() 사용
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //이미지가 로딩되면 into() 메서드로 imageView에 표사
        Glide.with(this).load(uri).into(imageView)
    }

    companion object {

        @JvmStatic
        //newInstance()이용해서 프래그먼트를 생성, 인자로 uri를 전달
        fun newInstance(uri: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI,uri)
                }
            }
    }
}