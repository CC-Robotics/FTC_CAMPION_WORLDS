package org.firstinspires.ftc.teamcode.opmode.auto

import com.pedropathing.follower.Follower
import com.pedropathing.localization.Pose
import com.pedropathing.pathgen.BezierCurve
import com.pedropathing.pathgen.BezierLine
import com.pedropathing.pathgen.PathChain
import com.pedropathing.pathgen.Point
import com.pedropathing.util.Constants
import com.rowanmcalpin.nextftc.core.command.Command
import com.rowanmcalpin.nextftc.core.command.groups.ParallelGroup
import com.rowanmcalpin.nextftc.core.command.groups.SequentialGroup
import com.rowanmcalpin.nextftc.core.command.utility.delays.Delay
import com.rowanmcalpin.nextftc.pedro.FollowPath
import com.rowanmcalpin.nextftc.pedro.PedroOpMode
import org.firstinspires.ftc.teamcode.subsystem.Arm
import org.firstinspires.ftc.teamcode.subsystem.Gripper
import org.firstinspires.ftc.teamcode.subsystem.Lift
import pedroPathing.constants.FConstants
import pedroPathing.constants.LConstants

class AutoCore: PedroOpMode(Arm, Gripper, Lift) {
    // Robot poses for navigation
    private val startPose = Pose(9.0, 111.0, Math.toRadians(270.0))    // Starting position
    private val scorePose = Pose(14.0, 129.0, Math.toRadians(315.0))   // Scoring position
    private val pickup1Pose = Pose(37.0, 121.0, Math.toRadians(0.0))   // First sample pickup
    private val pickup2Pose = Pose(37.0, 130.0, Math.toRadians(0.0))   // Second sample pickup
    private val pickup3Pose = Pose(37.0, 130.0, Math.toRadians(45.0))   // Third sample pickup
    private val parkPose = Pose(60.0, 98.0, Math.toRadians(90.0))      // Parking position
    private val parkControlPose = Pose(60.0, 98.0, Math.toRadians(90.0)) // Control point for curved path

    // Path variables
    private lateinit var moveToScore: PathChain              // Start → Score
    private lateinit var moveToPickup1: PathChain            // Score → Pickup1
    private lateinit var moveFromPickup1ToScore: PathChain   // Pickup1 → Score
    private lateinit var moveToPickup2: PathChain            // Score → Pickup2
    private lateinit var moveFromPickup2ToScore: PathChain   // Pickup2 → Score
    private lateinit var moveToPickup3: PathChain            // Score → Pickup3
    private lateinit var moveFromPickup3ToScore: PathChain   // Pickup3 → Score
    private lateinit var moveToPark: PathChain               // Score → Park

    /**
     * Builds all paths that the robot will follow during autonomous.
     *
     * Path Components:
     * - BezierLine: Creates a straight path between two points
     * - BezierCurve: Creates a curved path using control points
     *
     * Heading Interpolation: Determines how the robot's heading changes along the path:
     * - Linear: Gradually transitions between start and end headings
     * - Constant: Maintains a fixed heading
     * - Tangential: Aligns the heading with the path direction
     */
    private fun buildPaths() {
        // Initial path to scoring position
        moveToScore = follower.pathBuilder()
            .addPath(BezierLine(Point(startPose), Point(scorePose)))
            .setLinearHeadingInterpolation(startPose.heading, scorePose.heading)
            .build()

        // Path to first pickup
        moveToPickup1 = follower.pathBuilder()
            .addPath(BezierLine(Point(scorePose), Point(pickup1Pose)))
            .setLinearHeadingInterpolation(scorePose.heading, pickup1Pose.heading)
            .build()

        // Return from first pickup to score
        moveFromPickup1ToScore = follower.pathBuilder()
            .addPath(BezierLine(Point(pickup1Pose), Point(scorePose)))
            .setLinearHeadingInterpolation(pickup1Pose.heading, scorePose.heading)
            .build()

        // Path to second pickup
        moveToPickup2 = follower.pathBuilder()
            .addPath(BezierLine(Point(scorePose), Point(pickup2Pose)))
            .setLinearHeadingInterpolation(scorePose.heading, pickup2Pose.heading)
            .build()

        // Return from second pickup to score
        moveFromPickup2ToScore = follower.pathBuilder()
            .addPath(BezierLine(Point(pickup2Pose), Point(scorePose)))
            .setLinearHeadingInterpolation(pickup2Pose.heading, scorePose.heading)
            .build()

        // Path to third pickup
        moveToPickup3 = follower.pathBuilder()
            .addPath(BezierLine(Point(scorePose), Point(pickup3Pose)))
            .setLinearHeadingInterpolation(scorePose.heading, pickup3Pose.heading)
            .build()

        // Return from third pickup to score
        moveFromPickup3ToScore = follower.pathBuilder()
            .addPath(BezierLine(Point(pickup3Pose), Point(scorePose)))
            .setLinearHeadingInterpolation(pickup3Pose.heading, scorePose.heading)
            .build()

        // Path to parking
        moveToPark = follower.pathBuilder()
            .addPath(BezierCurve(
                Point(scorePose),
                Point(parkControlPose),
                Point(parkPose)))
            .setTangentHeadingInterpolation()
            .build()
    }

    /**
     * Command routine for initial scoring (preloaded element)
     */
    private val initialScoringRoutine: Command
        get() = SequentialGroup(
            ParallelGroup(
                FollowPath(moveToScore),
                Lift.toHigh,
                // Axel movement as well i believe but not sure
            ),
            Delay(0.15),
            //Claw related commands,
            Delay(0.15),
            Lift.toMiddle
        )

    /**
     * Command routine for first pickup and scoring cycle
     */
    private val pickup1Cycle: Command
        get() = SequentialGroup(
            // Go to pickup
            FollowPath(moveToPickup1),
            // Pickup
            ParallelGroup(
                Lift.toLow,
                // Close the Claw
            ),
            Delay(0.1),
            // Return to score
            ParallelGroup(
                FollowPath(moveFromPickup1ToScore),
                Lift.toHigh
            ),
            // Score
            Delay(0.15),
            // Open the Claw
        )

    /**
     * Command routine for second pickup and scoring cycle
     */
    private val pickup2Cycle: Command
        get() = SequentialGroup(
            // Go to pickup
            ParallelGroup(
                FollowPath(moveToPickup2),
                Lift.toMiddle
            ),
            // Pickup
            ParallelGroup(
                Lift.toLow,
                // Close the Claw
            ),
            Delay(0.1),
            // Return to score
            ParallelGroup(
                FollowPath(moveFromPickup2ToScore),
                Lift.toHigh
            ),
            // Score
            Delay(0.15),
            // Open the Claw
        )

    /**
     * Command routine for third pickup and scoring cycle
     */
    private val pickup3Cycle: Command
        get() = SequentialGroup(
            // Go to pickup
            ParallelGroup(
                FollowPath(moveToPickup3),
                Lift.toMiddle
            ),
            // Pickup
            ParallelGroup(
                Lift.toLow,
                // Close the Claw
            ),
            Delay(0.1),
            // Return to score
            ParallelGroup(
                FollowPath(moveFromPickup3ToScore),
                Lift.toHigh
            ),
            // Score
            Delay(0.15),
            // Open the Claw
        )

    /**
     * Command routine for parking
     */
    private val parkRoutine: Command
        get() = SequentialGroup(
            Lift.toMiddle,
            FollowPath(moveToPark)
        )

    /**
     * Complete autonomous routine combining all sequences
     */
    private val fullRoutine: Command
        get() = SequentialGroup(
            initialScoringRoutine,
            pickup1Cycle,
            pickup2Cycle,
            pickup3Cycle,
            parkRoutine
        )

    override fun onInit() {
       // Set constants, declaration of follower, and build paths
        Constants.setConstants(FConstants::class.java, LConstants::class.java)
        follower = Follower(hardwareMap)
        follower.setStartingPose(startPose)
        buildPaths()
    }

    override fun onStartButtonPressed() {
        // Finalize the follower and start the full routine
        fullRoutine()
    }
}