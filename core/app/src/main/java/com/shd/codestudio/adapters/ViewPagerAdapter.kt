package com.shd.codestudio.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shd.codestudio.artificial.agents.AIAgentManager
import com.shd.codestudio.fragments.ChatFragment
import com.shd.codestudio.fragments.AIHistoryFragment

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val aiAgent: AIAgentManager
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatFragment.newInstance(aiAgent)
            1 -> AIHistoryFragment.newInstance(aiAgent)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}