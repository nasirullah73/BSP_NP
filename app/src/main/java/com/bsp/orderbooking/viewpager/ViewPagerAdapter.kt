package com.bsp.orderbooking.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager)
    {
        private val mFragmentList: MutableList<ViewPagerItems> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position].fragment
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment?, title: String?) {
            mFragmentList.add(ViewPagerItems(title.toString(), fragment!!))
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentList[position].title
        }
    }