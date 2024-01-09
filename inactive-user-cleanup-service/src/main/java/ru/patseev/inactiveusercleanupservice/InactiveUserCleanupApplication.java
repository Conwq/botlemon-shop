package ru.patseev.inactiveusercleanupservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Сервис, который каждый 10 минут проверяет активированные аккаунты пользователей.
 */
@SpringBootApplication
public class InactiveUserCleanupApplication {

	public static void main(String[] args) {
		SpringApplication.run(InactiveUserCleanupApplication.class, args);
	}
}