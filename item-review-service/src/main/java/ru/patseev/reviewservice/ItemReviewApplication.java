package ru.patseev.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Стартовая точка сервиса комментариев и обзора предметов.
 */
@SpringBootApplication
public class ItemReviewApplication {
	public static void main(String[] args) {
		SpringApplication.run(ItemReviewApplication.class, args);
	}
}
