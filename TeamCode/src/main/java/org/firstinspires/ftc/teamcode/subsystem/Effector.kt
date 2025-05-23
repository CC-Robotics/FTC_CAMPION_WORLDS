package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.PwmControl.PwmRange
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.hardware.DualServo
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * The system controls the gripper of the bot which includes the claw servo, wrist yaw (left right) servos
 * and wrist pitch (up down) servos
 */
object Effector : SubsystemEx() {
    lateinit var claw: ServoImplEx

    private const val CLAW_NAME = "claw"

    private var currentClawState = ClawState.CLOSED

    object Wrist {
        lateinit var yaw: Servo
        lateinit var pitch: DualServo

        private const val WRIST_YAW_NAME = "wrist"

        private const val WRIST_PITCH_LEFT_NAME = "axle1"
        private const val WRIST_PITCH_RIGHT_NAME = "axle2"

        fun initialize() {
            yaw = OpModeData.hardwareMap!!.get(Servo::class.java, WRIST_YAW_NAME)
            // Use a DualServo because we use 2 servos for the wrist
            pitch = DualServo(WRIST_PITCH_LEFT_NAME, WRIST_PITCH_RIGHT_NAME)

            // Not sure what this does but precaution
            yaw.controller.pwmEnable()
            pitch.pwmEnabled = true

            pitch.follower.direction = Servo.Direction.REVERSE
        }
    }

    object Claw {
        fun initialize() {
            claw = OpModeData.hardwareMap!!.get(ServoImplEx::class.java, CLAW_NAME)
            claw.setPwmEnable()
            claw.pwmRange = PwmRange(500.0, 2500.0)
        }

        fun toggle() {
            setState(if (currentClawState == ClawState.CLOSED) ClawState.OPEN else ClawState.CLOSED)
        }

        private fun setState(state: ClawState) {
            currentClawState = state
            claw.position = state.position
        }

        val open: Command
            get() = InstantCommand { setState(ClawState.OPEN) }

        val close: Command
            get() = InstantCommand { setState(ClawState.CLOSED) }
    }


    override fun initialize() {
        Wrist.initialize()
        Claw.initialize()

        Wrist.yaw.position = 0.5
        Wrist.pitch.position = 0.3183
        Claw.close()
    }

    override fun attach(keymap: Keymap) {
        keymap.toggleClaw.pressedCommand = { InstantCommand { Claw.toggle() } }

        // Pitch controls are inverted
        keymap.upWristYaw.heldCommand = { InstantCommand { Wrist.yaw.position += 0.015 }}

        keymap.upWristPitch.pressedCommand = { InstantCommand { Wrist.pitch.position -= 0.01 }}
        keymap.upWristPitch.heldCommand = { InstantCommand { Wrist.pitch.position -= 0.005 }}

        keymap.downWristYaw.heldCommand = { InstantCommand { Wrist.yaw.position -= 0.015 }}

        keymap.downWristPitch.pressedCommand = { InstantCommand { Wrist.pitch.position += 0.01 }}
        keymap.downWristPitch.heldCommand = { InstantCommand { Wrist.pitch.position += 0.005 }}
    }

    override fun periodic() {
        RobotUtil.telemetry.addData("[Claw] Position", claw.position)
        RobotUtil.telemetry.addData("[Wrist] Yaw (twist) Position", Wrist.yaw.position)
        RobotUtil.telemetry.addData("[Wrist] Pitch Position", Wrist.pitch.position)
    }

    enum class ClawState(val position: Double) {
        OPEN(0.4),
        CLOSED(1.0)
    }
}