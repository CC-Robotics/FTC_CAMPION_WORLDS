package org.firstinspires.ftc.teamcode.archive.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import com.rowanmcalpin.nextftc.ftc.components.Components
import com.rowanmcalpin.nextftc.ftc.gamepad.GamepadManager
import org.firstinspires.ftc.teamcode.archive.subsystem.Arm
import org.firstinspires.ftc.teamcode.keymap.DefaultKeymap

@TeleOp(name = "ArmControl", group = "Test Modes")
class ArmControl : NextFTCOpMode() {
    override val components = Components()
        .useSubsystems(Arm)
        .useGamepads()

    override fun onStartButtonPressed() {
        class ArmKeymap : DefaultKeymap() {
            override val arm = GamepadManager.gamepad2.leftStick
        }
        Arm.attach(ArmKeymap())
    }

    override fun onUpdate() {
        telemetry.addData("Target Position", Arm.targetPosition)
        telemetry.addData("Current Position", Arm.armMotors.currentPosition)
        telemetry.update()
    }
}
