package jp.shiita.gamesample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_amida.*

class AmidaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_amida, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        amidaView.setOnClickListener { amidaView.invalidate() }
    }

    companion object {
        fun newInstance() = AmidaFragment()
    }
}
