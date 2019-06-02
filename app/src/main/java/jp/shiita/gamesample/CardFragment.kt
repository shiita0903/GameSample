package jp.shiita.gamesample

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card.*

class CardFragment : Fragment() {

    private val showEndListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            listOf(card1, card2, card3, card4).forEach { c ->
                c.setOnClickListener { startAnimation() }
            }
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationStart(animation: Animator?) {}
    }

    private val animationSet by lazy {
        AnimatorSet().apply {
            val cards = listOf(card1, card2, card3, card4)
            val delays = (0 until cards.size).map { it * 200L }
            val animators = cards.zip(delays).map { (c, d) -> getShowCardAnimation(c, d) }

            playTogether(animators)
            addListener(showEndListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_card, container, false)
    }

    override fun onStart() {
        super.onStart()
        startAnimation()
    }

    private fun startAnimation() {
        resetViewState()
        animationSet.start()
    }

    private fun resetViewState() {
        listOf(card1, card2, card3, card4).forEach { c ->
            c.scaleX = 0f
            c.scaleY = 0f
            c.setOnClickListener(null)
        }
    }

    private fun getShowCardAnimation(view: View, delay: Long): Animator {
        return AnimatorInflater.loadAnimator(context, R.animator.show_card).apply {
            setTarget(view)
            startDelay = delay
        }
    }

    companion object {
        fun newInstance() = CardFragment()
    }
}
