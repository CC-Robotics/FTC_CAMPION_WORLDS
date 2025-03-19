package org.firstinspires.ftc.teamcode.opmode.teleop

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.Arm
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Gripper
import org.firstinspires.ftc.teamcode.subsystem.Lift
import org.firstinspires.ftc.teamcode.subsystem.SubsystemEx


abstract class TeleOpCore : NextFTCOpMode(Drivetrain, Lift, Gripper, Arm) {
    private lateinit var keymap: Keymap

    override fun onInit() {
        keymap = DefaultKeymap(gamepadManager)
    }

    override fun onStartButtonPressed() {
        for (subsystem in subsystems)
            if (subsystem is SubsystemEx)
                subsystem.attach(gamepadManager, keymap)
    }
}