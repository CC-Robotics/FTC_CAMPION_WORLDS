package org.firstinspires.ftc.teamcode.opmode.teleop

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Effector
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.Pinion
import org.firstinspires.ftc.teamcode.util.RobotGlobals
import org.firstinspires.ftc.teamcode.util.RobotUtil

/**
 * The main TeleOp class for the robot. OpMode templating allows for easy splitting of the main
 * TeleOp logic based on the alliance colour.
 */
abstract class TeleOpCore : NextFTCOpMode() {
    private val subsystems = arrayOf(Drivetrain, Lift, Effector, Pinion)

    override val components = Components()
        .useGamepads()
        .useSubsystems(*subsystems)
        // Bulk reading yields large performance gains caching all data from control hub.
        .useBulkReading()

    override fun onInit() {
        RobotGlobals.isStarted = false
        // Uses the default keymap (basically named keybinds)
    }

    override fun onUpdate() {
        RobotUtil.telemetry.update()
    }

    override fun onStartButtonPressed() {
        RobotGlobals.isStarted = true
        RobotGlobals.keymap = DefaultKeymap()

        // Attach the keymap to all subsystems. (Binds them to the gamepad)
        for (subsystem in subsystems)
            subsystem.attach(RobotGlobals.keymap)
    }
}