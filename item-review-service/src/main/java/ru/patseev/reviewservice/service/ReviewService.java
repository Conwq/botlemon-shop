package ru.patseev.reviewservice.service;

import org.springframework.http.ResponseEntity;
import ru.patseev.reviewservice.dto.FeedbackRequest;
import ru.patseev.reviewservice.dto.InfoResponse;
import ru.patseev.reviewservice.dto.ReviewResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewService {

	BigDecimal getAverageItemRating(int itemId);

	ResponseEntity<InfoResponse> addReviewForItem(int userId, FeedbackRequest request);

	List<ReviewResponse> getAllReviewsForItem(int itemId);

	List<ReviewResponse> getAllReviewsFromSpecificUser(int userId);

	ResponseEntity<InfoResponse> editReviewForItem(int userId, FeedbackRequest request);
}
