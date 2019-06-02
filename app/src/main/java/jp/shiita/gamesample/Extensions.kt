package jp.shiita.gamesample

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.add(@IdRes id: Int, fragment: Fragment) = beginTransaction()
    .add(id, fragment)
    .commit()

fun FragmentManager.replace(@IdRes id: Int, fragment: Fragment, tag: String? = null) =
    beginTransaction()
        .replace(id, fragment)
        .addToBackStack(tag)
        .commit()
