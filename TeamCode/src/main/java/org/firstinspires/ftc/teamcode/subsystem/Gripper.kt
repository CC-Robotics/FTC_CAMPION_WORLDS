package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.Servo
import com.rowanmcalpin.nextftc.core.Subsystem
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.ftc.OpModeData
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.keymap.Keymap
import java.time.Instant

object Gripper : SubsystemEx() {
    private lateinit var claw: Servo
    private lateinit var wristYaw: Servo

    private lateinit var wristPitchLeft: Servo
    private lateinit var wristPitchRight: Servo

    private const val CLAW_NAME = "claw"
    private const val WRIST_YAW_NAME = "wrist"

    private const val WRIST_PITCH_LEFT_NAME = "axle1"
    private const val WRIST_PITCH_RIGHT_NAME = "axle2"

    private var currentClawState = ClawState.CLOSED


    override fun initialize() {
        claw = OpModeData.hardwareMap.get(Servo::class.java, CLAW_NAME)
        wristYaw = OpModeData.hardwareMap.get(Servo::class.java, WRIST_YAW_NAME)

        wristPitchLeft = OpModeData.hardwareMap.get(Servo::class.java, WRIST_PITCH_LEFT_NAME)
        wristPitchRight = OpModeData.hardwareMap.get(Servo::class.java, WRIST_PITCH_RIGHT_NAME)

        claw.controller.pwmEnable()
        wristYaw.controller.pwmEnable()
        wristPitchLeft.controller.pwmEnable()
        wristPitchRight.controller.pwmEnable()

        wristPitchRight.direction = Servo.Direction.REVERSE
    }

    fun toggleClaw() {
        currentClawState = if (currentClawState == ClawState.CLOSED) ClawState.OPEN else ClawState.CLOSED
        claw.position = currentClawState.position
    }

    val openClaw: Command
        get() = InstantCommand { setClawState(ClawState.OPEN) }

    val closeClaw: Command
        get() = InstantCommand { setClawState(ClawState.CLOSED) }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        keymap.toggleClaw.pressedCommand = { InstantCommand { toggleClaw() } }
    }

    private fun setClawState(state: ClawState) {
        currentClawState = state
        claw.position = state.position
    }

    enum class ClawState(val position: Double) {
        OPEN(0.4),
        CLOSED(1.0)
    }
}