package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.rowanmcalpin.nextftc.ftc.OpModeData
import kotlin.math.abs

/**
 * Wrapper class for motors that implements controllable (and can therefore be used with RunToPosition
 * commands).
 */
class ServoEx(val servo: ServoImplEx) {

    constructor(name: String): this(OpModeData.hardwareMap!!.get(ServoImplEx::class.java, name))

    private var cachedPosition = 0.0

    /**
     * The tolerance that must be surpassed in order to update the servos position. Defaults to 0.01.
     */
    var cacheTolerance = 0.001

    /**
     * Current velocity of the motor. Setter does nothing
     */
    var pwmEnabled: Boolean
        get() = servo.isPwmEnabled
        set(value) = if (value) servo.setPwmEnable() else servo.setPwmEnable()

    var direction: Servo.Direction
        get() = servo.direction
        set(value) { servo.direction = value }

    /**
     * Gets / sets the current position of the servo
     */
    var position: Double
        get() = cachedPosition
        set(value) {
            if (abs(cachedPosition - value) > cacheTolerance) {
                servo.position = value
                cachedPosition = value
            }
        }

    fun reverse(): ServoEx {
        direction = Servo.Direction.REVERSE
        return this
    }
}