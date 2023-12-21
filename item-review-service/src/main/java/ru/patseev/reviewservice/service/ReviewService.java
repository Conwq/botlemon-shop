package ru.patseev.reviewservice.service;

import org.springframework.http.ResponseEntity;
import ru.patseev.reviewservice.dto.FeedbackRequest;
import ru.patseev.reviewservice.dto.InfoResponse;
import ru.patseev.reviewservice.dto.ReviewResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewService {

	BigDecimal getAverageItemRating(int itemId);

	ResponseEntity<InfoResponse> addReviewAndRatingForItemFromUser(int userId, FeedbackRequest request);

	List<ReviewResponse> getAllReviewAndRatingForItemFromUsers(int itemId);

	List<ReviewResponse> getAllReviewAndRatingFromUserByUserId(int userId);

	ResponseEntity<InfoResponse> editReviewAndRatingForItem(int userId, FeedbackRequest request);
}
