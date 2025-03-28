package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.util.DualServo
import java.time.Instant

/**
 * The system controls the gripper of the bot which includes the claw servo, wrist yaw (left right) servos
 * and wrist pitch (up down) servos
 */
object Gripper : SubsystemEx() {
    private lateinit var claw: Servo
    private lateinit var wristYaw: Servo

    private lateinit var wristPitch: DualServo

    private const val CLAW_NAME = "claw"
    private const val WRIST_YAW_NAME = "wrist"

    private const val WRIST_PITCH_LEFT_NAME = "axle1"
    private const val WRIST_PITCH_RIGHT_NAME = "axle2"

    private var currentClawState = ClawState.CLOSED


    override fun initialize() {
        claw = OpModeData.hardwareMap.get(Servo::class.java, CLAW_NAME)
        wristYaw = OpModeData.hardwareMap.get(Servo::class.java, WRIST_YAW_NAME)

        // Use a DualServo because we use 2 servos for the wrist
        wristPitch = DualServo(WRIST_PITCH_LEFT_NAME, WRIST_PITCH_RIGHT_NAME)

        // Not sure what this does but precaution
        claw.controller.pwmEnable()
        wristYaw.controller.pwmEnable()
        wristPitch.pwmEnabled = true

        wristPitch.follower.direction = Servo.Direction.REVERSE
    }

    private fun toggleClaw() {
        setClawState(if (currentClawState == ClawState.CLOSED) ClawState.OPEN else ClawState.CLOSED)
    }

    private val toggleClawCommand: Command
        get() = InstantCommand { toggleClaw() }

    val openClaw: Command
        get() = InstantCommand { setClawState(ClawState.OPEN) }

    val closeClaw: Command
        get() = InstantCommand { setClawState(ClawState.CLOSED) }

    fun setClawState(state: ClawState) {
        currentClawState = state
        claw.position = state.position
    }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        keymap.toggleClaw.pressedCommand = { toggleClawCommand }
    }

    enum class ClawState(val position: Double) {
        OPEN(0.4),
        CLOSED(1.0)
    }
}