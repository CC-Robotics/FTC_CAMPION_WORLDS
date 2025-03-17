import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.rowanmcalpin.nextftc.ftc.OpModeData
import org.firstinspires.ftc.teamcode.opmode.teleop.TeleOpCore

@TeleOp(name = "Blue | Tele - Ri/Xa | Main", group = "2024-25")
class BlueTeleOp : TeleOpCore() {
    override fun onInit() {
        OpModeData.alliance = OpModeData.Alliance.BLUE
        super.onInit()
    }
}