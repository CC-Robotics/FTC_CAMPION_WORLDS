package org.firstinspires.ftc.teamcode.opmode.teleop.control

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorGroup
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Effector
import org.firstinspires.ftc.teamcode.subsystem.LiftRaw
import org.firstinspires.ftc.teamcode.subsystem.PinionRaw
import org.firstinspires.ftc.teamcode.util.RobotGlobals
import org.firstinspires.ftc.teamcode.util.RobotUtil

@TeleOp(name = "Everything Control", group = "Testing")
class EverythingControl : NextFTCOpMode() {
    private lateinit var liftLeft: MotorEx
    private lateinit var liftRight: MotorEx

    private lateinit var lift: MotorGroup

    private val subsystems = arrayOf(Effector, Drivetrain, PinionRaw, LiftRaw)

    override val components = Components()
        .useGamepads()
        .useSubsystems(*subsystems)
        .useBulkReading()

    lateinit var pinion: MotorEx

    /**
     * Describe this function...
     */
    override fun onInit() {
        RobotGlobals.isStarted = false

        liftLeft = MotorEx("lift_left")
        liftRight = MotorEx("lift_right")

        pinion = MotorEx("pinion")

        lift = RobotUtil.motorGroupFromNames("lift_right", "lift_left")
        lift.leader.reverse()
        lift.leader.resetEncoder()

        pinion.resetEncoder()
    }

    override fun onStartButtonPressed() {
        RobotGlobals.isStarted = true
        RobotGlobals.keymap = DefaultKeymap()
        // Attach the keymap to all subsystems. (Binds them to the gamepad)
        for (subsystem in subsystems)
            subsystem.attach(RobotGlobals.keymap)
    }

    override fun onUpdate() {
        telemetry.addData("pinion pos", PinionRaw.motor.currentPosition)
        telemetry.addData("lift", LiftRaw.motors.currentPosition)

        telemetry.addData("lift power", RobotGlobals.keymap.lift.y.toDouble())
        telemetry.addData("pinion power", RobotGlobals.keymap.pinion.y.toDouble())

        telemetry.addData("claw pos", Effector.claw.position)
        telemetry.addData("wrist yaw (twist) pos", Effector.Wrist.yaw.position)
        telemetry.addData("wrist pitch (tilt) pos", Effector.Wrist.pitch.position)

        telemetry.update()
    }
}