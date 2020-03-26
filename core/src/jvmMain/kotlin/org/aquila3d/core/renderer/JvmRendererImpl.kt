package org.aquila3d.core.renderer

import org.aquila3d.core.device.DeviceSelector
import org.aquila3d.core.device.FirstDeviceSelector
import org.aquila3d.core.vulkan.VkDebugUtilsMessengerCallback
import org.aquila3d.core.vulkan.VkDebugUtilsMessengerCallbackCreateInfo
import org.aquila3d.core.vulkan.VkDevice

open class JvmRendererImpl: Renderer.RendererImpl {

    override fun configureDebug(requiredExtensions: MutableList<String>): VkDebugUtilsMessengerCallbackCreateInfo {
        requiredExtensions.add(Renderer.VK_EXT_DEBUG_UTILS_EXTENSION_NAME)
        return VkDebugUtilsMessengerCallbackCreateInfo(createDebugCallback())
    }

    override fun getDeviceSelector(): DeviceSelector {
        return FirstDeviceSelector()
    }

    override fun createLogicalDevice(requiredExtensions: List<String>): VkDevice {
        TODO("Not yet implemented")
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun createDebugCallback(): VkDebugUtilsMessengerCallback {
        return VkDebugUtilsMessengerCallback()
    }
}