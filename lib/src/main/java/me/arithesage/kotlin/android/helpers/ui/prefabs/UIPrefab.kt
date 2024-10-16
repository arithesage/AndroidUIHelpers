@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package me.arithesage.kotlin.android.helpers.ui.prefabs

import android.content.Context
import android.view.ViewGroup


abstract class UIPrefab<T:ViewGroup> (appContext: Context?) {
    protected lateinit var ui: T

    init {
        if (appContext != null) {
            this.setup (appContext)
        }
    }


    protected abstract fun setup (appContext: Context)


    open fun setupLayoutFor (viewGroup: T) {

    }


    fun ui (): T {
        return ui
    }
}