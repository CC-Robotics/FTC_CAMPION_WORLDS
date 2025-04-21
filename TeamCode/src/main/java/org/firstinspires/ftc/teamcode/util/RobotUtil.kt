package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.Controllable
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import org.firstinspires.ftc.teamcode.command.RunToPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap

object RobotUtil {
    /**
     * Create a MotorGroup from motor names of a leader and followers. This method is used instead
     * of the constructor override as the logic contains a known fatal bug.
     */
    fun motorGroupFromNames(leader: String, vararg followers: String): MotorGroup {
        return MotorGroup(MotorEx(leader), * Array(followers.size) { i -> MotorEx(followers[i]) })
    }

    lateinit var keymap: Keymap

    fun handleControl(name: String, useControl: Boolean,  motor: Controllable, controlSystem: ControlSystem, targetPosition: Double, runToPosition: RunToPosition, deadzone: Double = 0.0) {
        if (useControl) {
            if (deadzone != 0.0) {
                if (controlSystem.isWithinTolerance(KineticState(deadzone))) {
                    motor.power = 0.0
                } else {
                    motor.power =
                        controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
                }
            } else motor.power =
                controlSystem.calculate(KineticState(motor.currentPosition, motor.velocity))
            if (targetPosition != controlSystem.goal.position) {
                runToPosition()
            }
        } else {
            motor.power = 0.0
        }

        telemetry.addData("[$name] Target Position", targetPosition)
        telemetry.addData("[$name] Current Position", motor.currentPosition)
        telemetry.addData("[$name] Power", motor.power)
    }

    @JvmField val telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, OpModeData.telemetry)
}