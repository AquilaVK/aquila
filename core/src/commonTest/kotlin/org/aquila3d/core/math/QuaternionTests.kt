package org.aquila3d.core.math

import kotlin.math.abs
import kotlin.test.*

class QuaternionTests {

    @Test
    fun testDefaultConstructor() {
        val q = Quaternion()
        assertNotNull(q)
        assertEquals(0.0, q.w, "Invalid W component")
        assertEquals(0.0, q.x, "Invalid X component")
        assertEquals(1.0, q.y, "Invalid Y component")
        assertEquals(0.0, q.z, "Invalid Z component")
    }

    @Test
    fun testFloatComponentConstructor() {
        val q = Quaternion(1.0f, 2.0f, 3.0f, 4.0f)
        assertNotNull(q)
        assertEquals(1.0, q.w, "Invalid W component")
        assertEquals(2.0, q.x, "Invalid X component")
        assertEquals(3.0, q.y, "Invalid Y component")
        assertEquals(4.0, q.z, "Invalid Z component")
    }

    @Test
    fun testDoubleComponentConstructor() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        assertNotNull(q)
        assertEquals(1.0, q.w, "Invalid W component")
        assertEquals(2.0, q.x, "Invalid X component")
        assertEquals(3.0, q.y, "Invalid Y component")
        assertEquals(4.0, q.z, "Invalid Z component")
    }

    @Test
    fun testQuaternionConstructor() {
        val from = Quaternion(1.0, 2.0, 3.0, 4.0)
        val output = Quaternion(from)
        assertNotNull(output)
        assertNotSame(from, output, "Should not be the same instance.")
        assertEquals(1.0, output.w, "Invalid W component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(3.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testAxisAngleConstructor() {
        val sqrt2o2 = 0.70710678118654752440084436210485

        // Test 90 degrees about X
        val x90 = Quaternion(Degrees(90.0).toRadians(), Vector3.X())
        assertNotNull(x90)
        assertTrue(abs(sqrt2o2 - x90.w) <= 1e-12, "Invalid W component: " + x90.w)
        assertTrue(abs(sqrt2o2 - x90.x) <= 1e-12, "Invalid X component: " + x90.x)
        assertEquals(0.0, x90.y, "Invalid Y component")
        assertEquals(0.0, x90.z, "Invalid Z component")

        // Test 90 degrees about Y
        val y90 = Quaternion(Degrees(90.0).toRadians(), Vector3.Y())
        assertNotNull(y90)
        assertTrue(abs(sqrt2o2 - y90.w) <= 1e-12, "Invalid W component: " + y90.w)
        assertEquals(0.0, y90.x, "Invalid X component")
        assertTrue(abs(sqrt2o2 - y90.y) <= 1e-12, "Invalid Y component: " + y90.y)
        assertEquals(0.0, y90.z, "Invalid Z component")

        // Test 90 degrees about Z
        val z90 = Quaternion(Degrees(90.0).toRadians(), Vector3.Z())
        assertNotNull(z90)
        assertTrue(abs(sqrt2o2 - z90.w) <= 1e-12, "Invalid W component: " + z90.w)
        assertEquals(0.0, z90.x, "Invalid X component")
        assertEquals(0.0, z90.y, "Invalid Y component")
        assertTrue(abs(sqrt2o2 - z90.z) <= 1e-12, "Invalid Z component: " + z90.z)

        // Test 30 degrees about an <1, 1, 1> vector.
        val q = Quaternion(Degrees(30.0).toRadians(), Vector3(1.0, 1.0, 1.0))
        assertNotNull(q)
        assertTrue(abs(0.9659258262890683 - q.w) <= 1e-12, "Invalid W component: " + q.w)
        assertTrue(abs(0.14942924536134225 - q.x) <= 1e-12, "Invalid X component: " + q.x)
        assertTrue(abs(0.14942924536134225 - q.y) < 1e-12, "Invalid Y component: " + q.y)
        assertTrue(abs(0.14942924536134225 - q.z) <= 1e-12, "Invalid Z component: " + q.z)

        // Test 30 degrees about an even <-1, -1, -1> vector.
        val p = Quaternion(Degrees(30.0).toRadians(), Vector3(-1.0, -1.0, -1.0))
        assertNotNull(p)
        assertTrue(abs(0.9659258262890683 - p.w) <= 1e-12, "Invalid W component: " + q.w)
        assertTrue(abs(-0.14942924536134225 - p.x) <= 1e-12, "Invalid X component: " + q.x)
        assertTrue(abs(-0.14942924536134225 - p.y) < 1e-12, "Invalid Y component: " + q.y)
        assertTrue(abs(-0.14942924536134225 - p.z) <= 1e-12, "Invalid Z component: " + q.z)
    }

    @Test
    fun testEulerAngleConstructor() {
        val sqrt2o2 = 0.70710678118654752440084436210485

        // Test 90 degrees about X
        val x90 = Quaternion(Radians(0.0), Degrees(90.0).toRadians(), Radians(0.0))
        assertNotNull(x90)
        assertTrue(abs(sqrt2o2 - x90.w) <= 1e-12, "x90 Invalid W component: $x90")
        assertEquals(0.0, x90.x, "x90 Invalid X component $x90")
        assertTrue(abs(sqrt2o2 - x90.y) <= 1e-12, "x90 Invalid Y component: $x90")
        assertEquals(0.0, x90.z, "x90 Invalid Z component $x90")

        // Test 90 degrees about Y
        val y90 = Quaternion(Degrees(90.0).toRadians(), Radians(0.0), Radians(0.0))
        assertNotNull(y90)
        assertTrue(abs(sqrt2o2 - y90.w) <= 1e-12, "y90 Invalid W component: " + y90.w)
        assertEquals(0.0, y90.x, "y90 Invalid X component")
        assertEquals(0.0, y90.y, "y90 Invalid Y component")
        assertTrue(abs(sqrt2o2 - y90.z) <= 1e-12, "y90 Invalid Z component: " + y90.z)

        // Test 90 degrees about Z
        val z90 = Quaternion(Radians(0.0), Radians(0.0), Degrees(90.0).toRadians())
        assertNotNull(z90)
        assertTrue(abs(sqrt2o2 - z90.w) <= 1e-12, "z90 Invalid W component: " + z90.w)
        assertTrue(abs(sqrt2o2 - z90.x) <= 1e-12, "z90 Invalid X component: " + z90.x)
        assertEquals(0.0, z90.y, "z90 Invalid Y component")
        assertEquals(0.0, z90.z, "z90 Invalid Z component")

        // Test 30 degrees about Y, 60 about X, 90 about Z
        val q = Quaternion(Degrees(30.0).toRadians(), Degrees(60.0).toRadians(), Degrees(90.0).toRadians())
        assertNotNull(q)
        assertTrue(abs(0.6830127018922193 - q.w) <= 1e-12, "q Invalid W component: " + q.w)
        assertTrue(abs(0.5 - q.x) <= 1e-12, "q Invalid X component: " + q.x)
        assertTrue(abs(0.5 - q.y) <= 1e-12, "q Invalid Y component: " + q.y)
        assertTrue(abs(-0.18301270189221924 - q.z) <= 1e-12, "q Invalid Z component: " + q.z)
    }

    @Test
    fun testUnaryMinusOperator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        -q
        assertEquals(1.0, q.w, "Invalid W component")
        assertEquals(-2.0, q.x, "Invalid X component")
        assertEquals(-3.0, q.y, "Invalid Y component")
        assertEquals(-4.0, q.z, "Invalid Z component")
    }

    @Test
    fun testPlusOperator() {
        val q = Quaternion(1.0, 2.0,3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q + p
        assertSame(q, output, "Not the same instance.")
        assertEquals(6.0, output.w, "Invalid W component")
        assertEquals(8.0, output.x, "Invalid X component")
        assertEquals(10.0, output.y, "Invalid Y component")
        assertEquals(12.0, output.z, "Invalid Z component")
    }

    @Test
    fun testMinusOperator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q - p
        assertSame(q, output, "Not the same instance.")
        assertEquals(-4.0, output.w, "Invalid W component")
        assertEquals(-4.0, output.x, "Invalid X component")
        assertEquals(-4.0, output.y, "Invalid Y component")
        assertEquals(-4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testTimesFloatOperator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val output = q * 2f
        assertSame(q, output, "Not the same instance.")
        assertEquals(2.0, output.w, "Invalid W component")
        assertEquals(4.0, output.x, "Invalid X component")
        assertEquals(6.0, output.y, "Invalid Y component")
        assertEquals(8.0, output.z, "Invalid Z component")
    }

    @Test
    fun testTimesDoubleOperator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val output = q * 3.0
        assertSame(q, output, "Not the same instance.")
        assertEquals(3.0, output.w, "Invalid X component")
        assertEquals(6.0, output.x, "Invalid X component")
        assertEquals(9.0, output.y, "Invalid Y component")
        assertEquals(12.0, output.z, "Invalid Z component")
    }

    @Test
    fun testTimesVector3Operator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val v = Vector3(5.0, 6.0, 7.0)
        val output = q * v
        assertSame(q, output, "Not the same instance.")
        assertEquals(-56.0, output.w, "Invalid X component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(12.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testTimesQuaternionOperator() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q * p
        assertSame(q, output, "Not the same instance.")
        assertEquals(-60.0, output.w, "Invalid X component")
        assertEquals(12.0, output.x, "Invalid X component")
        assertEquals(30.0, output.y, "Invalid Y component")
        assertEquals(24.0, output.z, "Invalid Z component")
    }

    @Test
    fun testDivFloatOperator() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val output = q / 2f
        assertSame(q, output, "Not the same instance.")
        assertEquals(1.0, output.w, "Invalid W component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(3.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testDivDoubleOperator() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val output = q / 2.0
        assertSame(q, output, "Not the same instance.")
        assertEquals(1.0, output.w, "Invalid W component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(3.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testGetAxis() {
        val axis = Vector3.X()
        val angle = Degrees(30.0).toRadians()
        val q = Quaternion(angle, axis)
        val output = q.getAxis()
        assertTrue(abs(axis.x - output.x) <= 1e-12, "Invalid X component: " + output.x)
        assertTrue(abs(axis.y - output.y) <= 1e-12, "Invalid Y component: " + output.y)
        assertTrue(abs(axis.z - output.z) <= 1e-12, "Invalid Z component: " + output.z)
    }

    @Test
    fun testGetAngle() {
        val axis = Vector3.X()
        val angle = Degrees(30.0).toRadians()
        val q = Quaternion(angle, axis)
        val output = q.getAngle()
        assertTrue(abs((angle - output).angle) <= 1e-12, "Invalid X component: $output")
    }

    @Test
    fun testGetMagnitude() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val magnitude = q.magnitude()
        assertTrue(abs(10.954451150103322269139395656016 - magnitude) <= 1e-12, "Invalid magnitude: $magnitude")
    }

    @Test
    fun testAdd() {
        val q = Quaternion(1.0, 2.0,3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q.add(p)
        assertSame(q, output, "Not the same instance.")
        assertEquals(6.0, output.w, "Invalid W component")
        assertEquals(8.0, output.x, "Invalid X component")
        assertEquals(10.0, output.y, "Invalid Y component")
        assertEquals(12.0, output.z, "Invalid Z component")
    }

    @Test
    fun testSubtract() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q.subtract(p)
        assertSame(q, output, "Not the same instance.")
        assertEquals(-4.0, output.w, "Invalid W component")
        assertEquals(-4.0, output.x, "Invalid X component")
        assertEquals(-4.0, output.y, "Invalid Y component")
        assertEquals(-4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testMultiplyFloat() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val output = q.multiply(2f)
        assertSame(q, output, "Not the same instance.")
        assertEquals(2.0, output.w, "Invalid W component")
        assertEquals(4.0, output.x, "Invalid X component")
        assertEquals(6.0, output.y, "Invalid Y component")
        assertEquals(8.0, output.z, "Invalid Z component")
    }

    @Test
    fun testMultiplyDouble() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val output = q.multiply(3.0)
        assertSame(q, output, "Not the same instance.")
        assertEquals(3.0, output.w, "Invalid X component")
        assertEquals(6.0, output.x, "Invalid X component")
        assertEquals(9.0, output.y, "Invalid Y component")
        assertEquals(12.0, output.z, "Invalid Z component")
    }

    @Test
    fun testMultiplyVector3() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val v = Vector3(5.0, 6.0, 7.0)
        val output = q.multiply(v)
        assertSame(q, output, "Not the same instance.")
        assertEquals(-56.0, output.w, "Invalid X component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(12.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testMultiplyQuaternion() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        val p = Quaternion(5.0, 6.0, 7.0, 8.0)
        val output = q.multiply(p)
        assertSame(q, output, "Not the same instance.")
        assertEquals(-60.0, output.w, "Invalid X component")
        assertEquals(12.0, output.x, "Invalid X component")
        assertEquals(30.0, output.y, "Invalid Y component")
        assertEquals(24.0, output.z, "Invalid Z component")
    }

    @Test
    fun testDivideFloat() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val output = q.divide(2f)
        assertSame(q, output, "Not the same instance.")
        assertEquals(1.0, output.w, "Invalid W component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(3.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testDivideDouble() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val output = q.divide(2.0)
        assertSame(q, output, "Not the same instance.")
        assertEquals(1.0, output.w, "Invalid W component")
        assertEquals(2.0, output.x, "Invalid X component")
        assertEquals(3.0, output.y, "Invalid Y component")
        assertEquals(4.0, output.z, "Invalid Z component")
    }

    @Test
    fun testNormalize() {
        val q = Quaternion(2.0, 4.0, 6.0, 8.0)
        val output = q.normalize()
        val magnitude = output.magnitude()
        val error = abs(magnitude - 1.0)
        assertSame(q, output, "Not the same instance.")
        assertTrue(1e-12 >= error, "Invalid magnitude error after normalizing: $error")
    }

    @Test
    fun testInvert() {
        val q = Quaternion(1.0, 2.0, 3.0, 4.0)
        q.invert()
        assertEquals(1.0, q.w, "Invalid W component")
        assertEquals(-2.0, q.x, "Invalid X component")
        assertEquals(-3.0, q.y, "Invalid Y component")
        assertEquals(-4.0, q.z, "Invalid Z component")
    }

    @Test
    fun testRotateVector() {

    }
}