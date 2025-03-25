package pedroPathing.constants

import com.pedropathing.localization.Encoder
import com.pedropathing.localization.constants.DriveEncoderConstants

object LConstants {
    init {
        DriveEncoderConstants.forwardTicksToInches = 1.0;
        DriveEncoderConstants.strafeTicksToInches = 1.0;
        DriveEncoderConstants.turnTicksToInches = 1.0;

        DriveEncoderConstants.robot_Width = 16.5;
        DriveEncoderConstants.robot_Length = 13.0;

        DriveEncoderConstants.leftFrontEncoderDirection = Encoder.REVERSE;
        DriveEncoderConstants.rightFrontEncoderDirection = Encoder.FORWARD;
        DriveEncoderConstants.leftRearEncoderDirection = Encoder.REVERSE;
        DriveEncoderConstants.rightRearEncoderDirection = Encoder.FORWARD;
    }
}