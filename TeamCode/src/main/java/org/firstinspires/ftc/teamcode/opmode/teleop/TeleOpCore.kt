package org.firstinspires.ftc.teamcode.opmode.teleop

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Effector
import org.firstinspires.ftc.teamcode.subsystem.Lifts
import org.firstinspires.ftc.teamcode.subsystem.Pinion

/**
 * The main TeleOp class for the robot. OpMode templating allows for easy splitting of the main
 * TeleOp logic based on the alliance colour.
 */
abstract class TeleOpCore : NextFTCOpMode() {
    private val subsystems = arrayOf(Drivetrain, Lifts, Effector, Pinion)

    override val components = Components()
        .useSubsystems(*subsystems)
        .useGamepads()
        // Bulk reading yields large performance gains caching all data from control hub.
        .useBulkReading()
    private lateinit var keymap: Keymap

    override fun onInit() {
        // Uses the default keymap (basically named keybinds)
        keymap = DefaultKeymap()
    }

    override fun onStartButtonPressed() {
        // Attach the keymap to all subsystems. (Binds them to the gamepad)
        for (subsystem in subsystems)
            subsystem.attach(keymap)
    }
}