package org.firstinspires.ftc.teamcode.opmode.teleop

import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap
import org.firstinspires.ftc.teamcode.keymap.Keymap
import org.firstinspires.ftc.teamcode.subsystem.Arm
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Lift


open class TeleOpCore: NextFTCOpMode(Drivetrain, Lift) {
    private lateinit var keymap: Keymap

    override fun onInit() {
        keymap = DefaultKeymap(gamepadManager)
    }

    override fun onStartButtonPressed() {
        Drivetrain.attach(gamepadManager.gamepad1)
        Lift.attach(keymap)
        Arm.attach(keymap)
    }
}