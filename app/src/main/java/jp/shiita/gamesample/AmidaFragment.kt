package jp.shiita.gamesample

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.fragment_amida.*

class AmidaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_amida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ObjectAnimator().apply {
            propertyName = "positionY"
            duration = 2000
            target = amidaView
            interpolator = LinearInterpolator()
            setFloatValues(0f, 1f)
            start()
        }
        listOf(text1, text2, text3, text4, text5)
            .forEachIndexed { i, t -> t.setOnClickListener { amidaView.drawLine(i) } }
    }

    companion object {
        fun newInstance() = AmidaFragment()
    }
}
