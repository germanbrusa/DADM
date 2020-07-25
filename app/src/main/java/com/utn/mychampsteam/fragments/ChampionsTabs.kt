package com.utn.mychampsteam.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.utn.mychampsteam.R

class ChampionsTabs : Fragment() {

    lateinit var v: View
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.fragment_champions_tabs, container, false)

        tabLayout = v.findViewById(R.id.tab_layout)
        viewPager = v.findViewById(R.id.view_pager)
        // Inflate the layout for this fragment
        return v
    }

    override fun onStart() {
        super.onStart()

        viewPager.setAdapter(CreateCardAdapter())
        TabLayoutMediator( tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> tab.text = "Team Champs"
                1 -> tab.text = "All Champs"
                else -> tab.text = "undefined"
            }
        }).attach()
    }

    private fun CreateCardAdapter(): ViewPagerAdapter? {
        return ViewPagerAdapter(requireActivity())
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> TeamChampions()
                1 -> AllChampions()
                else -> TeamChampions()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }
}
