package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.RedeemRequest;
import com.guide_wire.demo.entity.Reward;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.service.RewardService;
import com.guide_wire.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final UserService userService;

    /**
     * 📜 Get reward history
     */
    @GetMapping
    public List<Reward> getUserRewards() {
        User user = userService.getCurrentUser();
        return rewardService.getUserRewards(user);
    }

    /**
     * 📊 Get total points
     */
    @GetMapping("/points")
    public int getTotalPoints() {
        User user = userService.getCurrentUser();
        return rewardService.getTotalPoints(user);
    }

    /**
     * 💸 Redeem points
     */
    @PostMapping("/redeem")
    public String redeemPoints(@RequestBody RedeemRequest request) {

        User user = userService.getCurrentUser();

        rewardService.redeemPoints(
                user,
                request.getPoints(),
                request.getReason()
        );

        return "Points redeemed successfully";
    }
}