package jp.shiita.gamesample

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.support.annotation.AnimatorRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card.*

class CardFragment : Fragment() {
    private var isStarted = false

    private val showEndListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            listOf(
                card_front1 to card_back1,
                card_front2 to card_back2,
                card_front3 to card_back3,
                card_front4 to card_back4
            ).forEach { (f, b) ->
                b.setOnClickListener {
                    listOf(card_back1, card_back2, card_back3, card_back4).forEach {
                        it.setOnClickListener(null)
                    }

                    val animatorF = getFlipCardAnimator(f, R.animator.flip_in)
                    val animatorB = getFlipCardAnimator(b, R.animator.flip_out)
                    AnimatorSet().apply {
                        playTogether(listOf(animatorF, animatorB))
                        start()
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                showCoinGetDialog()
                            }
                        })
                    }
                }
            }
        }
    }

    private val animationSet by lazy {
        AnimatorSet().apply {
            val cards = listOf(card_back1, card_back2, card_back3, card_back4)
            val delays = (0 until cards.size).map { it * 200L }
            val animators = cards.zip(delays).map { (c, d) -> getShowCardAnimator(c, d) }

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
        if (!isStarted) {
            isStarted = true
            startAnimation()
        }
    }

    private fun startAnimation() {
        resetViewState()
        animationSet.start()
    }

    private fun resetViewState() {
        listOf(card_back1, card_back2, card_back3, card_back4).forEach { c ->
            c.scaleX = 0f
            c.scaleY = 0f
            c.setOnClickListener(null)
        }
    }

    private fun getShowCardAnimator(view: View, delay: Long): Animator {
        return AnimatorInflater.loadAnimator(context, R.animator.show_card).apply {
            setTarget(view)
            startDelay = delay
        }
    }

    private fun getFlipCardAnimator(view: View, @AnimatorRes id: Int) =
        AnimatorInflater.loadAnimator(context, id).apply {
            setTarget(view)
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
        fun newInstance() = CardFragment()
    }
}
