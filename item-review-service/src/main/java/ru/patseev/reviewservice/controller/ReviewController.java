package ru.patseev.reviewservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.patseev.jwtservice.starter.service.JwtHeader;
import ru.patseev.jwtservice.starter.service.JwtService;
import ru.patseev.reviewservice.dto.FeedbackRequest;
import ru.patseev.reviewservice.dto.InfoResponse;
import ru.patseev.reviewservice.dto.ReviewResponse;
import ru.patseev.reviewservice.service.ReviewService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Контроллер отвечающий за работу сервиса обзоров предметов.
 */
@RestController
@RequestMapping("/v1/api/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	private final JwtService jwtService;

	/**
	 * Получает среднюю оценку товара.
	 *
	 * @param itemId Идентификатор предмета, среднюю оценку которого вернуть.
	 * @return Среднюю оценку предмета.
	 */
	@GetMapping("/average_rating/{itemId}")
	public ResponseEntity<BigDecimal> getAverageItemRating(@PathVariable("itemId") int itemId) {
		BigDecimal averageItemRating = reviewService.getAverageItemRating(itemId);
		return ResponseEntity.ok(averageItemRating);
	}

	/**
	 * Получает все обзоры пользователей для конкретного предмета.
	 *
	 * @param itemId Идентификатор предмета, обзоры на который нужно получить.
	 * @return Список обзоров от пользователей.
	 */
	@GetMapping("/reviews/{itemId}")
	public ResponseEntity<List<ReviewResponse>> getAllReviewsForItem(@PathVariable("itemId") int itemId) {
		List<ReviewResponse> allReviewForItem = reviewService.getAllReviewsForItem(itemId);
		return ResponseEntity.ok(allReviewForItem);
	}

	/**
	 * Получает все обзоры пользователя.
	 *
	 * @param header Заголовок запроса (JSON Web Token).
	 * @return Список обзоров от пользователей.
	 */
	@GetMapping("/user_reviews")
	public ResponseEntity<List<ReviewResponse>> getAllReviewsFromSpecificUser(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {

		final String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		List<ReviewResponse> allReviewForItemFromUser = reviewService.getAllReviewsFromSpecificUser(userId);
		return ResponseEntity.ok(allReviewForItemFromUser);
	}

	/**
	 * Добавляет обзор от пользователя.
	 *
	 * @param header  Заголовок запроса (JSON Web Token).
	 * @param request Запрос на добавление обзора.
	 * @return Возвращает информацию о добавлении обзора для предмета.
	 */
	@PostMapping("/add")
	public ResponseEntity<InfoResponse> addReviewForItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String header,
														 @Valid
														 @RequestBody FeedbackRequest request) {
		final String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		return reviewService.addReviewForItem(userId, request);
	}

	/**
	 * Изменяет опубликованный обзор от пользователя.
	 *
	 * @param header  Заголовок запроса (JSON Web Token).
	 * @param request Запрос на обновление обзора.
	 * @return Возвращает информацию об изменении обзора предмета.
	 */
	@PatchMapping("/edit")
	public ResponseEntity<InfoResponse> editReviewForItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String header,
														  @RequestBody FeedbackRequest request) {
		final String token = header.replace(JwtHeader.BEARER, "");
		int userId = jwtService.extractUserId(token);

		return reviewService.editReviewForItem(userId, request);
	}
}