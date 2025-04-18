package org.firstinspires.ftc.teamcode.archive.subsystem

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.clamp
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.InstantCommand
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.core.units.TimeSpan
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.command.RunToPosition
import dev.nextftc.nextcontrol.KineticState
import dev.nextftc.nextcontrol.builder.controlSystem
import dev.nextftc.nextcontrol.feedback.PIDCoefficients
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.SubsystemEx
import org.firstinspires.ftc.teamcode.util.RobotUtil

@Config
/**
 * The Arm system is what controls the pivot of the robot in which the
 * lift (linear slide) system is attached to. It uses a PIDF controller
 * with gain scheduled feedforward to ensure motions are smooth and accurate.
 * */
object Arm : SubsystemEx() {
    lateinit var armMotors: MotorGroup

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
    var kF = 0.0005

    @JvmField
    var targetPosition = 0.0

    /**
     * Boolean indicating if the control system should update the controllable
     * in the periodic function of the subsystem in each loop.
     * */
    @JvmField
    var useControl = true

    @JvmField
    var coefficients = PIDCoefficients(0.0, 0.0, 0.0)

    /**
     * The control system of the arm (pivot) which uses a PID controller in combination
     * with gain-scheduled feedforward to ensure movement is accurate.
     * */
    @JvmField
//    var controlSystem = ControlSystem(
//        PIDElement(PIDType.POSITION, 0.008, 0.002, 0.0002),
//        { input ->
//            val thetaDegrees = 80 + (80 * input.position / 1010)
//            val thetaRadians = Math.toRadians(thetaDegrees)
//            feedforwardMultiplier * sin((Math.PI / 2) - thetaRadians)
//        },
//        FilterElement(),
//        ConstantInterpolator(KineticState())
//    )
    var controlSystem = controlSystem {
        posPid(coefficients)
        armFF(kF)
    }

    private val runToPosition: RunToPosition
        get() = RunToPosition(targetPosition, controlSystem, this)

    private val resetEncoder: SequentialGroup
        get() = SequentialGroup(
            RunToPosition(0.0, controlSystem, this),
            InstantCommand {
                targetPosition = 0.0
                useControl = false
            },
            Delay(TimeSpan.Companion.fromMs(450)),
            InstantCommand {
                armMotors.leader.resetEncoder()
                useControl = true
            }
        )

    override fun periodic() {
        if (useControl) {
            armMotors.power = controlSystem.calculate(KineticState(armMotors.currentPosition, armMotors.velocity))
        }
    }

    override fun initialize() {
        targetPosition = 0.0

        armMotors = RobotUtil.motorGroupFromNames("right", "left")
        armMotors.leader.reverse() // So they go in the right direction
        armMotors.leader.resetEncoder() // Good practice
    }

    override fun attach(keymap: Keymap) {
        /*
        * On gamepad joystick movement (displacement), update the
        * target position of the arm by how much the joystick was
        * moved upwards then run to that position
        * */
        keymap.arm.heldCommand = { xy ->
            targetPosition += xy.second
            targetPosition = clamp(targetPosition, 0.0, Double.POSITIVE_INFINITY)
            runToPosition
        }
    }
}