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

/**
 * Сервис по работе с обзорами на предметы.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final ReviewMapper reviewMapper;

	/**
	 * Получает средний рейтинг предмета.
	 *
	 * @param itemId Идентификатор предмета.
	 * @return Значение среднего рейтинга в формате Х.Х.
	 */
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

	/**
	 * Добавляет обзор на предмет.
	 *
	 * @param userId  Идентификатор пользователя, который добавляет обзор.
	 * @param request Объект с оценкой и комментарием.
	 * @return Возвращает объект ответа со статусом и сообщением.
	 */
	@Override
	@Transactional
	public ResponseEntity<InfoResponse> addReviewForItem(int userId, FeedbackRequest request) {
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

	/**
	 * Получает все обзоры на конкретный предмет.
	 *
	 * @param itemId Идентификатор предмета.
	 * @return Возвращает список комментариев предмета.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getAllReviewsForItem(int itemId) {
		return itemRepository
				.findById(itemId)
				.orElseThrow(ItemNotFoundException::new)
				.getReviewEntityList()
				.stream()
				.map(reviewMapper::toDto)
				.toList();
	}

	/**
	 * Получает все обзоры для конкретного пользователя.
	 *
	 * @param userId Идентификатор пользователя, для которого нужно получить обзоры.
	 * @return Возвращает список комментариев предмета оставленные определенным пользователем.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getAllReviewsFromSpecificUser(int userId) {
		return userRepository
				.findById(userId)
				.orElseThrow(UserNotFoundException::new)
				.getReviewEntityList()
				.stream()
				.map(reviewMapper::toDto)
				.toList();
	}

	/**
	 * Меняет обзор на предмет.
	 * Обзор может менять лишь тот пользователь, который оставил этот комментарий.
	 *
	 * @param userId  Идентификатор пользователя, который оставляет обзор.
	 * @param request Запрос на изменение обзора.
	 * @return Возвращает тело ответо со статусом и сообщением.
	 */
	@Override
	@Transactional
	public ResponseEntity<InfoResponse> editReviewForItem(int userId, FeedbackRequest request) {
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

	/*
	 * Проверяет, оставлял ли данный пользователь комментарий под этим предметом.
	 * Комментарий можно оставлять под предметом лишь один раз.
	 */
	private void checkForDuplicateReview(int userId, ItemEntity itemEntity) {
		List<ReviewEntity> reviewEntityList = itemEntity.getReviewEntityList();

		if (reviewEntityList.stream()
				.anyMatch(reviewEntity -> reviewEntity.getUserEntity().getId() == userId)) {
			throw new DuplicateReviewException("This user has already left a review");
		}
	}

	/*
	 * Создает ответ
	 */
	private ResponseEntity<InfoResponse> createResponse(Actions action, HttpStatus status) {
		InfoResponse infoResponse = new InfoResponse(action, status);
		return new ResponseEntity<>(infoResponse, status);
	}

	/*
	 * Меняет поля у предмета.
	 */
	private ReviewEntity editFieldReviewEntity(int userId, FeedbackRequest request) {
		ReviewEntity reviewEntity = reviewRepository
				.findById(request.id())
				.orElseThrow(ReviewNotFoundException::new);

		Integer reviewUserId = reviewEntity.getUserEntity().getId();

		if (userId != reviewUserId) {
			throw new UserMismatchException("User cannot change comments of other users");
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