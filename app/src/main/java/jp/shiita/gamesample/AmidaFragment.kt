package jp.shiita.gamesample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
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
        listOf(text1, text2, text3, text4, text5)
            .forEachIndexed { i, t ->
                t.setOnClickListener {
                    listOf(text1, text2, text3, text4, text5).forEach { t ->
                        t.setOnClickListener(null)
                    }

                    val len = amidaView.calcPoints(i)
                    ObjectAnimator().apply {
                        propertyName = "progress"
                        duration = (len * 2).toLong()
                        target = amidaView
                        interpolator = LinearInterpolator()
                        setFloatValues(0f, 1f)
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                showCoinGetDialog()
                            }
                        })
                        start()
                    }
                }
            }
    }

    private fun showCoinGetDialog() {
        val act = activity ?: return
        AlertDialog.Builder(act)
            .setMessage("10コイン獲得しました！")
            .setPositiveButton("OK") { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .setOnCancelListener { fragmentManager?.popBackStack() }
            .show()
    }

    companion object {
        fun newInstance() = AmidaFragment()
    }
}
