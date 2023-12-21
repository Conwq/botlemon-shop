package ru.patseev.reviewservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.reviewservice.dto.FeedbackRequest;
import ru.patseev.reviewservice.dto.InfoResponse;
import ru.patseev.reviewservice.dto.ReviewResponse;
import ru.patseev.reviewservice.service.ReviewService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping("/rating/{itemId}")
	public ResponseEntity<BigDecimal> getAverageItemRating(@PathVariable("itemId") int itemId) {
		BigDecimal averageItemRating = reviewService.getAverageItemRating(itemId);
		return ResponseEntity.ok(averageItemRating);
	}

	@PostMapping("/{userId}")
	public ResponseEntity<InfoResponse> addReviewAndRatingForItemFromUser(@PathVariable("userId") int userId,
																		  @RequestBody FeedbackRequest request) {
		return reviewService.addReviewAndRatingForItemFromUser(userId, request);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<List<ReviewResponse>> getAllReviewAndRatingForItemFromUsers(@PathVariable("itemId") int itemId) {
		List<ReviewResponse> allReviewForItem = reviewService.getAllReviewAndRatingForItemFromUsers(itemId);
		return ResponseEntity.ok(allReviewForItem);
	}

	@GetMapping("/user_review/{userId}")
	public ResponseEntity<List<ReviewResponse>> getAllReviewAndRatingFromUserByUserId(@PathVariable("userId") int userId) {
		List<ReviewResponse> allReviewForItemFromUser = reviewService.getAllReviewAndRatingFromUserByUserId(userId);
		return ResponseEntity.ok(allReviewForItemFromUser);
	}

	@PatchMapping("/{userId}")
	public ResponseEntity<InfoResponse> editReviewAndRatingForItem(@PathVariable("userId") int userId,
																   @RequestBody FeedbackRequest request) {
		return reviewService.editReviewAndRatingForItem(userId, request);
	}
}
