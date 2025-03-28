package pedroPathing.constants

import com.pedropathing.follower.FollowerConstants
import com.pedropathing.localization.Localizers
import com.qualcomm.robotcore.hardware.DcMotorSimple

/*
* This is tuning information for PedroPathing, allowing for
* accurate autonomic movement. These most likely do not need
* to be changed unless issues are found with motion.
* */

object FConstants {
    init {
        FollowerConstants.localizers = Localizers.DRIVE_ENCODERS

        FollowerConstants.leftFrontMotorName = "fL"
        FollowerConstants.leftRearMotorName = "bL"
        FollowerConstants.rightFrontMotorName = "fR"
        FollowerConstants.rightRearMotorName = "bR"

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD

        FollowerConstants.mass = 13.0

        FollowerConstants.xMovement = 57.8741
        FollowerConstants.yMovement = 52.295

        FollowerConstants.forwardZeroPowerAcceleration = -41.278
        FollowerConstants.lateralZeroPowerAcceleration = -59.7819

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.1, 0.0, 0.01, 0.0)
        FollowerConstants.useSecondaryTranslationalPID = false
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(
            0.1,
            0.0,
            0.01,
            0.0
        ) // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(2.0, 0.0, 0.1, 0.0)
        FollowerConstants.useSecondaryHeadingPID = false
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(
            2.0,
            0.0,
            0.1,
            0.0
        ) // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.1, 0.0, 0.0, 0.6, 0.0)
        FollowerConstants.useSecondaryDrivePID = false
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(
            0.1,
            0.0,
            0.0,
            0.6,
            0.0
        ) // Not being used, @see useSecondaryDrivePID

        FollowerConstants.zeroPowerAccelerationMultiplier = 4.0
        FollowerConstants.centripetalScaling = 0.0005

        FollowerConstants.pathEndTimeoutConstraint = 500.0
        FollowerConstants.pathEndTValueConstraint = 0.995
        FollowerConstants.pathEndVelocityConstraint = 0.1
        FollowerConstants.pathEndTranslationalConstraint = 0.1
        FollowerConstants.pathEndHeadingConstraint = 0.007
    }
}