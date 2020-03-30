package org.aquila3d.core.device

import org.aquila3d.core.surface.Surface
import org.aquila3d.core.vulkan.VkInstance
import org.aquila3d.core.vulkan.VkPhysicalDevice
import org.aquila3d.core.vulkan.VkQueueFamilies

fun deviceSelectorError(message: String, selector: DeviceSelector, queueFamily: VkQueueFamilies): IllegalStateException {
    return IllegalStateException("$message Cause: missing ${queueFamily.simpleName} command queue family index. This is a " +
            "bug in the used DeviceSelector (${selector::class.qualifiedName}) as the selected VkPhysicalDevice does " +
            "not support a required queue family. If you are using a DeviceSelector implementation provided by " +
            "AquilaVK, please report this at https://github.com/AquilaVK/aquila/issues")
}

interface DeviceSelector {

    fun select(
        surface: Surface,
        requiredQueueFamilies: List<VkQueueFamilies>,
        requiredDeviceExtensions: List<String>
    ): VkPhysicalDevice?
}
