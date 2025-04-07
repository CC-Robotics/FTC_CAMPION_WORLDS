package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.DualServo

/**
 * The system controls the gripper of the bot which includes the claw servo, wrist yaw (left right) servos
 * and wrist pitch (up down) servos
 */
object Effector : SubsystemEx() {
    lateinit var claw: Servo
    lateinit var wristYaw: Servo

    lateinit var wristPitch: DualServo

    private const val CLAW_NAME = "claw"

    private var currentClawState = ClawState.CLOSED

    object Wrist {
        private const val WRIST_YAW_NAME = "wrist"

        private const val WRIST_PITCH_LEFT_NAME = "axle1"
        private const val WRIST_PITCH_RIGHT_NAME = "axle2"

        fun initialize() {
            wristYaw = OpModeData.hardwareMap!!.get(Servo::class.java, WRIST_YAW_NAME)

            // Use a DualServo because we use 2 servos for the wrist
            wristPitch = DualServo(WRIST_PITCH_LEFT_NAME, WRIST_PITCH_RIGHT_NAME)

            // Not sure what this does but precaution
            wristYaw.controller.pwmEnable()
            wristPitch.pwmEnabled = true

            wristPitch.follower.direction = Servo.Direction.REVERSE
        }
    }

    object Claw {
        fun initialize() {
            claw = OpModeData.hardwareMap!!.get(Servo::class.java, CLAW_NAME)
            claw.controller.pwmEnable()
        }

        fun toggle() {
            setClawState(if (currentClawState == ClawState.CLOSED) ClawState.OPEN else ClawState.CLOSED)
        }

        val open: Command
            get() = InstantCommand { setClawState(ClawState.OPEN) }

        val close: Command
            get() = InstantCommand { setClawState(ClawState.CLOSED) }
    }


    override fun initialize() {
        Wrist.initialize()
        Claw.initialize()
    }



    private fun setClawState(state: ClawState) {
        currentClawState = state
        claw.position = state.position
    }

    override fun attach(keymap: Keymap) {
        keymap.toggleClaw.pressedCommand = { InstantCommand { Claw.toggle() } }
    }

    enum class ClawState(val position: Double) {
        OPEN(0.4),
        CLOSED(1.0)
    }
}