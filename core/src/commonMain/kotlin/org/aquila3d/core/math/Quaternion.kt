package org.aquila3d.core.math

import kotlin.jvm.JvmStatic
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A Unit [Quaternion] is used to represent a rotation/orientation in 3D space.
 *
 * Assuming the [Quaternion]s in question are normalized (unit length), subsequent rotations can be performed by
 * multiplying quaternions together. If they are not normalized, then the provided [rotate] functions must be used.
 *
 * Note that [Quaternion] math is associative but not commutative - i.e.:
 *
 * **q**(**p****r**) == (**q****p**)**r** but **q****p** != **p****q**
 *
 * It is advantages to represent orientations with [Quaternion]s as they are not subject to Gimble Lock like Euler
 * rotations, provide smooth interpolation between orientations and reduce the number of computations needed for any
 * given mutation.
 */
class Quaternion {

    var w: Double
        private set
    var x: Double
        private set
    var y: Double
        private set
    var z: Double
        private set

    /**
     * Constructs a unit quaternion representing 0 rotation. By default the axis of rotation is the +Y axis.
     */
    constructor() {
        w = 0.0
        x = 0.0
        y = 1.0
        z = 0.0
    }

    /**
     * Constructs a new [Quaternion] from the provided [Float] components.
     */
    constructor(w: Float, x: Float, y: Float, z: Float): this(w.toDouble(), x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Constructs a new [Quaternion] from the provided [Double] components.
     */
    constructor(w: Double, x: Double, y: Double, z: Double) {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
    }

    /**
     * Constructs a new unit [Quaternion] from a provided [Quaternion].
     */
    constructor(other: Quaternion) {
        this.w = other.w
        this.x = other.x
        this.y = other.y
        this.z = other.z
    }

    /**
     * Constructs a new unit [Quaternion] from a given rotation about a given axis. The inputs do not need to be
     * normalized.
     *
     * @param angle Angle of ratation in [Radians]
     * @param axis [Vector3] representing the axis of rotation
     */
    constructor(angle: Radians, axis: Vector3) {
        val s = sin(angle.angle / 2.0)
        val normAxis = Vector3(axis).normalize() * s
        w = cos(angle.angle / 2.0)
        x = normAxis.x
        y = normAxis.y
        z = normAxis.z
    }

    /**
     * Constructs a new [Quaternion] by applying the provided Euler rotation angles in order, yaw - pitch - roll.
     * Note that these Euler angles are defined in GL coordinate space, not the typical rigid body mechanics space.
     *
     * @param yaw Rotation about the +Y axis, in [Radians]
     * @param pitch Rotation about the +X Axis, in [Radians]
     * @param roll Rotation about the +Z Axis, in [Radians]
     */
    constructor(yaw: Radians, pitch: Radians, roll: Radians) {
        val hYaw = yaw.angle / 2.0
        val hPitch = pitch.angle / 2.0
        val hRoll = roll.angle / 2.0
        val sYaw = sin(hYaw)
        val cYaw = cos(hYaw)
        val sPitch = sin(hPitch)
        val cPitch = cos(hPitch)
        val sRoll = sin(hRoll)
        val cRoll = cos(hRoll)

        val cYawcPitch = cYaw * cPitch
        val sYawsPitch = sYaw * sPitch
        val cYawsPitch = cYaw * sPitch
        val sYawcPitch = sYaw * cPitch

        w = cRoll * cYawcPitch + sRoll * sYawsPitch
        x = cRoll * cYawsPitch + sRoll * sYawcPitch
        y = cRoll * sYawcPitch - sRoll * cYawsPitch
        z = sRoll * cYawcPitch - cRoll * sYawsPitch
    }

    operator fun unaryMinus() {
        invert()
    }

    operator fun plus(other: Quaternion): Quaternion {
        w += other.w
        x += other.x
        y += other.y
        z += other.z
        return this
    }

    operator fun minus(other: Quaternion): Quaternion {
        w -= other.w
        x -= other.x
        y -= other.y
        z -= other.z
        return this
    }

    operator fun times(scale: Float): Quaternion = times(scale.toDouble())

    operator fun times(scale: Double): Quaternion {
        w *= scale
        x *= scale
        y *= scale
        z *= scale
        return this
    }

    operator fun times(vector3: Vector3): Quaternion {
        val wNew = -(x * vector3.x + y * vector3.y + z * vector3.z)
        val xNew = w * vector3.x + y * vector3.z - z * vector3.y
        val yNew = w * vector3.y + z * vector3.x - x * vector3.z
        val zNew = w * vector3.z + x * vector3.y - y * vector3.x
        w = wNew
        x = xNew
        y = yNew
        z = zNew
        return this
    }

    operator fun times(other: Quaternion): Quaternion {
        val wNew = w * other.w - x * other.x - y * other.y - z * other.z
        val xNew = w * other.x + x * other.w + y * other.z - z * other.y
        val yNew = w * other.y + y * other.w + z * other.x - x * other.z
        val zNew = w * other.z + z * other.w + x * other.y - y * other.x
        w = wNew
        x = xNew
        y = yNew
        z = zNew
        return this
    }

    operator fun div(scale: Float): Quaternion = div(scale.toDouble())

    operator fun div(scale: Double): Quaternion {
        w /= scale
        x /= scale
        y /= scale
        z /= scale
        return this
    }

    /**
     * Gets the unit axis of rotation for this quaternion.
     *
     * @return Normalized [Vector3] representing the axis of rotation.
     */
    fun getAxis(): Vector3 {
        if (w == 1.0) {
            // This is a 0 degree rotation and any vector is valid, we default to +Y
            return Vector3(0.0, 1.0, 0.0)
        }
        val divisor = sqrt(1 - w * w)
        return (Vector3(x, y, z) / divisor)
    }

    /**
     * Gets the angle of rotation in radians for this quaternion.
     *
     * @return The angle of rotation in [Radians].
     */
    fun getAngle(): Radians = Radians(2.0 * acos(w))

    /**
     * Calculates the magnitude (norm) of this [Quaternion]
     *
     * @return The magnitude (norm).
     */
    fun magnitude(): Double = sqrt(w * w + x * x + y * y + z * z)

    fun normalize(): Quaternion {
        val magnitude = magnitude()
        return div(magnitude)
    }

    fun invert(): Quaternion {
        x = -x
        y = -y
        z = -z
        return this
    }

    /**
     * Rotates [vector3] by the rotation represented in this [Quaternion]. Mathematically this is equivalent to:
     *
     * **v'** = **q** * **v** * ~**q** where ~**q** is the conjugate or inverse of this [Quaternion].
     */
    fun rotate(vector3: Vector3): Vector3 {
        val rotation = Quaternion(this)
        val conj = Quaternion(this).invert()
        return ((rotation * vector3) * conj).getAxis()
    }

    override fun toString(): String {
        return "Quaternion(w=$w, x=$x, y=$y, z=$z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Quaternion

        if (w != other.w) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = w.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    companion object {

        /**
         * Constructs a new [Quaternion] by applying the provided Euler rotation angles in order, yaw - pitch - roll.
         * Note that these Euler angles are defined in GL coordinate space, not the typical rigid body mechanics space.
         *
         * @param yaw Rotation about the +Y axis, in [Degrees]
         * @param pitch Rotation about the +X Axis, in [Degrees]
         * @param roll Rotation about the +Z Axis, in [Degrees]
         */
        @JvmStatic
        fun fromEulerAnglesDeg(yaw: Degrees, pitch: Degrees, roll: Degrees): Quaternion
                = Quaternion(yaw.toRadians(), pitch.toRadians(), roll.toRadians())

        /**
         * Constructs a new [Quaternion] by applying the provided Euler rotation angles in order, yaw - pitch - roll.
         * Note that these Euler angles are defined in GL coordinate space, not the typical rigid body mechanics space.
         *
         * @param yaw Rotation about the +Y axis, in [Radians]
         * @param pitch Rotation about the +X Axis, in [Radians]
         * @param roll Rotation about the +Z Axis, in [Radians]
         */
        @JvmStatic
        fun fromEulerAnglesRad(yaw: Radians, pitch: Radians, roll: Radians): Quaternion = Quaternion(yaw, pitch, roll)
    }
}