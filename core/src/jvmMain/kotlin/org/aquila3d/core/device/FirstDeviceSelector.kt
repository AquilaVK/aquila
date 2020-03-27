package org.aquila3d.core.device

import org.aquila3d.core.surface.Surface
import org.aquila3d.core.vulkan.VkInstance
import org.aquila3d.core.vulkan.VkPhysicalDevice
import org.aquila3d.core.vulkan.VkQueueFamilies
import org.aquila3d.core.vulkan.VkResult
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.vulkan.VK10.VK_SUCCESS
import org.lwjgl.vulkan.VK10.vkEnumeratePhysicalDevices

//TODO: This class could do better by prioritizing devices which are either discrete (or not) GPUs as well as favoring
// devices which support multiple queue families in the same queue index for performance
actual
class FirstDeviceSelector : DeviceSelector {

    override fun select(
        surface: Surface,
        requiredQueueFamilies: List<VkQueueFamilies>,
        requiredDeviceExtensions: List<String>
    ): VkPhysicalDevice? {
        val instance = surface.instance
        val devices = enumeratePhysicalDevices(instance)
        var returnDevice: VkPhysicalDevice? = null
        for (devicePointer in devices) {
            val device = org.lwjgl.vulkan.VkPhysicalDevice(devicePointer, instance.instance)
            val physicalDevice = VkPhysicalDevice(device, surface)
            if (ensureQueueSupport(physicalDevice, requiredQueueFamilies) && ensureDeviceExtensions(
                    physicalDevice,
                    requiredDeviceExtensions
                )
            ) {
                returnDevice = physicalDevice
                break
            }
        }
        return returnDevice
    }

    private fun ensureQueueSupport(
        physicalDevice: VkPhysicalDevice,
        requiredQueueFamilies: List<VkQueueFamilies>
    ): Boolean {
        return physicalDevice.getQueueFamilyIndices().keys.containsAll(requiredQueueFamilies)
    }

    private fun ensureDeviceExtensions(
        physicalDevice: VkPhysicalDevice,
        requiredDeviceExtensions: List<String>
    ): Boolean {
        return physicalDevice.getDeviceExtensions().keys.containsAll(requiredDeviceExtensions)
    }

    private fun enumeratePhysicalDevices(instance: VkInstance): List<Long> {
        val list = mutableListOf<Long>()

        val pPhysicalDeviceCount = memAllocInt(1)
        var err = vkEnumeratePhysicalDevices(instance.instance, pPhysicalDeviceCount, null)
        if (err != VK_SUCCESS) {
            throw AssertionError("Failed to get number of physical devices: " + VkResult(err))
        }
        val pPhysicalDevices = memAllocPointer(pPhysicalDeviceCount.get(0))
        err = vkEnumeratePhysicalDevices(instance.instance, pPhysicalDeviceCount, pPhysicalDevices)
        if (err != VK_SUCCESS) {
            throw AssertionError("Failed to get physical devices: ${VkResult(err)}")
        }
        for (i in 0 until pPhysicalDeviceCount.get(0)) {
            list.add(pPhysicalDevices.get(i))
        }

        // Free native pointers
        memFree(pPhysicalDeviceCount)
        memFree(pPhysicalDevices)
        return list
    }
}