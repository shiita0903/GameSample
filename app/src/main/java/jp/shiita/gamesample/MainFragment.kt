package jp.shiita.gamesample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardGameButton.setOnClickListener {
            fragmentManager?.replace(R.id.mainContainer, CardFragment.newInstance())
        }
        amidaGameButton.setOnClickListener {
            fragmentManager?.replace(R.id.mainContainer, AmidaFragment.newInstance())
        }
        lotteryGameButton.setOnClickListener {
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
