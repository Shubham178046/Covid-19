package com.example.covid19.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.covid19.fragment.ViewPager1Fragment
import com.example.covid19.fragment.ViewPager2Fragment
import com.example.covid19.fragment.ViewPager3Fragment

class TabViewPagerAdapter(var fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
       return 3
    }

    override fun getItem(position: Int): Fragment {
      when(position)
      {
          0->
          {
              return ViewPager1Fragment.getInstance()
          }
          1->
          {
              return ViewPager2Fragment.getInstance()
          }
          2->
          {
              return ViewPager3Fragment.getInstance()
          }
          else->{
              return null!!
          }
      }
    }
}