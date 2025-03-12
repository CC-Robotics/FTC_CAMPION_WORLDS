package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.subsystem.Lift

@TeleOp(name = "NextFTC TeleOp Program Kotlin")
class TeleOp: NextFTCOpMode(Drivetrain, Lift) {
    override fun onStartButtonPressed() {
        Drivetrain.attach(gamepadManager)
        Lift.attach(gamepadManager)
    }
}