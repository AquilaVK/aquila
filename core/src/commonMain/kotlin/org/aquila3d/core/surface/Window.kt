package org.aquila3d.core.surface

expect class Window(width: Int, height: Int, title: String) {

    fun destroy()

    fun onResized(width: Int, height: Int)
}