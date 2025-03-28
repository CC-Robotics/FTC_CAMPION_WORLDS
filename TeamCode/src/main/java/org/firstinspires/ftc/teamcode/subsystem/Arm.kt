package org.firstinspires.ftc.teamcode.subsystem

import com.acmerobotics.dashboard.config.Config
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.command.RunToPosition
import dev.nextftc.nextcontrol.ControlSystem
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.feedback.PIDElement
import dev.nextftc.nextcontrol.feedback.PIDType
import dev.nextftc.nextcontrol.filters.FilterElement
import dev.nextftc.nextcontrol.interpolators.ConstantInterpolator
import org.firstinspires.ftc.teamcode.keymap.Keymap
import kotlin.math.roundToInt
import kotlin.math.sin

@Config
/**
 * The Arm system is what controls the pivot of the robot in which the
 * lift (linear slide) system is attached to. It uses a PIDF controller
 * with gain scheduled feedforward to ensure motions are smooth and accurate.
 * */
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

    /**
     * Dashboard editable default feedforward multiplier.
     * It is multiplied by the sin of the arm angle as well as the lift extension
     * percentage to calculate the correct feedforward term.
     * */
    @JvmField
    var feedforwardMultiplier = 0.0005

    @JvmField
    var targetPosition = 0

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = true

    /**
     * The control system of the arm (pivot) which uses a PID controller in combination
     * with gain-scheduled feedforward to ensure movement is accurate.
     * */
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
        get() = RunToPosition(targetPosition.toDouble(), controlSystem, this)

    override fun periodic() {
        if (useControl) {
            arm.power = controlSystem.calculate(KineticState(arm.currentPosition, arm.velocity))
        }
    }

    override fun initialize() {
        arm = MotorGroup("right", "left")
        arm.leader.reverse() // So they go in the right direction
        arm.leader.resetEncoder() // Good practice
    }

    override fun attach(gamepadManager: GamepadManager, keymap: Keymap) {
        /*
        * On gamepad joystick movement (displacement), update the
        * target position of the arm by how much the joystick was
        * moved upwards then run to that position
        * */
        keymap.arm.displacedCommand = { xy ->
            targetPosition += (xy.second * 5).roundToInt()
            runToPosition
        }
    }
}