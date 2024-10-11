package me.arithesage.kotlin.android.helpers.ui

import android.view.View
import android.view.ViewGroup

object Utils {
    fun ChildrenOf (view: View?) : List<View> {
        val visitedNodes = ArrayList<View> ()
        val pendingNodes = ArrayList<View> ()

        if (view != null) {
            pendingNodes.add (view)

            while (!pendingNodes.isEmpty()) {
                val child = pendingNodes.removeAt (0)
                visitedNodes.add (child)

                if (!(child is ViewGroup)) {
                    continue
                }

                val viewGroup = (child as ViewGroup)
                val children = viewGroup.childCount

                for (c in 0..children) {
                    pendingNodes.add (viewGroup.getChildAt (c))
                }
            }
        }

        return visitedNodes
    }
}













