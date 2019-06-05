package jp.shiita.gamesample

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.constraint.motion.MotionLayout
import android.support.constraint.motion.MotionScene
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_lottery.*
import kotlin.random.Random

class LotteryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lottery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        octagonImage.setOnClickListener {
            octagonImage.setOnClickListener(null)

            AnimatorInflater.loadAnimator(context, R.animator.rotate).apply {
                setTarget(octagonImage)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        startMotion()
                    }
                })
                start()
            }
        }
    }

    private fun startMotion() {
        motionLayout.visibility = View.VISIBLE
        val colors = listOf(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent
        )
        circle.setColorFilter(
            ResourcesCompat.getColor(
                resources,
                colors[Random.nextInt(3)],
                null
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )

        motionLayout.transitionToEnd()
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

            override fun allowsTransition(p0: MotionScene.Transition?): Boolean = true

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                showCoinGetDialog()
            }
        })
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
        fun newInstance() = LotteryFragment()
    }
}
