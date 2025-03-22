package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.command.RunToPosition
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.command.HoldPosition
import org.firstinspires.ftc.teamcode.keymap.Keymap
import kotlin.math.roundToInt
import kotlin.math.sin

@Config
object Arm : SubsystemEx() {
    private lateinit var arm: MotorGroup

//    private val controller = PIDFController(0.008, 0.002, 0.0002, GainScheduledArmFeedforward({
//        1.0 // Replace with Lift.motor.currentPosition calculation
//    }, { input ->
//
//        val thetaDegrees = 80 + (80 * input / 1010) // Convert encoder input to degrees
//        val thetaRadians = Math.toRadians(thetaDegrees)    // Convert to radians
//        (Math.PI / 2) - thetaRadians           // Adjust to arm feedforward logic
//    }))

    @JvmField
    var feedforwardMultiplier = 0.0005

    @JvmField
    var position = 0

    @JvmField
    var useControl = true

    private val controlSystem = ControlSystem(
        PIDElement(PIDType.POSITION, 0.008, 0.002, 0.0002),
        { input ->
            val thetaDegrees = 80 + (80 * input.position / 1010)
            val thetaRadians = Math.toRadians(thetaDegrees)
            feedforwardMultiplier * sin((Math.PI / 2) - thetaRadians)
        },
        FilterElement(),
        ConstantInterpolator(KineticState())
    )

    private val runToPosition: RunToPosition
        get() = RunToPosition(position.toDouble(), controlSystem, this)

    override fun periodic() {
        if (useControl) {
            arm.power = controlSystem.calculate(KineticState(arm.currentPosition, arm.velocity))
        }
    }

    override fun initialize() {
        arm = MotorGroup("right", "left")
        arm.leader.reverse()
        arm.leader.resetEncoder()
    }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        keymap.arm.displacedCommand = { xy ->
            position += (xy.second * 5).roundToInt()
            runToPosition
        }
    }
}