package ru.patseev.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.reviewservice.domain.ItemEntity;
import ru.patseev.reviewservice.domain.ReviewEntity;
import ru.patseev.reviewservice.domain.UserEntity;
import ru.patseev.reviewservice.dto.Actions;
import ru.patseev.reviewservice.dto.FeedbackRequest;
import ru.patseev.reviewservice.dto.InfoResponse;
import ru.patseev.reviewservice.dto.ReviewResponse;
import ru.patseev.reviewservice.exception.*;
import ru.patseev.reviewservice.mapper.ReviewMapper;
import ru.patseev.reviewservice.repository.ItemRepository;
import ru.patseev.reviewservice.repository.ReviewRepository;
import ru.patseev.reviewservice.repository.UserRepository;
import ru.patseev.reviewservice.service.ReviewService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final ReviewMapper reviewMapper;

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getAverageItemRating(int itemId) {
		final int voters;

		List<ReviewEntity> allReviewEntityForItem = reviewRepository
				.findAllByItemEntityId(itemId);

		if ((voters = allReviewEntityForItem.size()) == 0) {
			return BigDecimal.ZERO;
		}

		return allReviewEntityForItem.stream()
				.map(ReviewEntity::getRating)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(voters), 1, RoundingMode.HALF_UP);
	}

	@Override
	@Transactional
	public ResponseEntity<InfoResponse> addReviewAndRatingForItemFromUser(int userId, FeedbackRequest request) {
		ItemEntity itemEntity = itemRepository
				.findById(request.id())
				.orElseThrow(ItemNotFoundException::new);
		UserEntity userEntity = userRepository
				.findById(userId)
				.orElseThrow(UserNotFoundException::new);

		this.checkForDuplicateReview(userId, itemEntity);
		ReviewEntity reviewEntity = this.createReviewEntity(itemEntity, userEntity, request);
		reviewRepository.save(reviewEntity);

		return this.createResponse(Actions.ADD_REVIEW, HttpStatus.CREATED);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getAllReviewAndRatingForItemFromUsers(int itemId) {
		return itemRepository
				.findById(itemId)
				.orElseThrow(ItemNotFoundException::new)
				.getReviewEntityList()
				.stream()
				.map(reviewMapper::toDto)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getAllReviewAndRatingFromUserByUserId(int userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(UserNotFoundException::new)
				.getReviewEntityList()
				.stream()
				.map(reviewMapper::toDto)
				.toList();
	}

	@Override
	@Transactional
	public ResponseEntity<InfoResponse> editReviewAndRatingForItem(int userId, FeedbackRequest request) {
		ReviewEntity reviewEntity = this.editFieldReviewEntity(userId, request);
		reviewRepository.save(reviewEntity);

		return this.createResponse(Actions.EDIT_REVIEW, HttpStatus.OK);
	}

	private ReviewEntity createReviewEntity(ItemEntity itemEntity, UserEntity userEntity, FeedbackRequest request) {
		ReviewEntity reviewEntity = ReviewEntity.builder()
				.itemEntity(itemEntity)
				.userEntity(userEntity)
				.review(request.review())
				.rating(request.rating())
				.build();

		itemEntity.addReviewEntity(reviewEntity);
		userEntity.addReviewEntity(reviewEntity);

		return reviewEntity;
	}

	private void checkForDuplicateReview(int userId, ItemEntity itemEntity) {
		List<ReviewEntity> reviewEntityList = itemEntity.getReviewEntityList();

		if (reviewEntityList.stream()
				.anyMatch(reviewEntity -> reviewEntity.getUserEntity().getId() == userId)) {
			throw new DuplicateReviewException("This user has already left a review");
		}
	}

	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}

	private ReviewEntity editFieldReviewEntity(int userId, FeedbackRequest request) {
		ReviewEntity reviewEntity = reviewRepository
				.findById(request.id())
				.orElseThrow(ReviewNotFoundException::new);

		Integer reviewUserId = reviewEntity.getUserEntity().getId();

		if (userId != reviewUserId) {
			throw new UserMismatchException();
		}

		if (Objects.nonNull(request.review())) {
			reviewEntity.setReview(request.review());
		}

		if (Objects.nonNull(request.rating())) {
			reviewEntity.setRating(request.rating());
		}

		return reviewEntity;
	}
}