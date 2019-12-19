package com.khb.mvvmlibrary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *创建时间：2019/12/9
 *编写人：kanghb
 *功能描述：
 */
class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment =
        fragments.get(position)


    override fun getCount(): Int =
        fragments.size

}