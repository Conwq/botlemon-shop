package ru.patseev.inactiveusercleanupservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.patseev.inactiveusercleanupservice.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Сервис для работы с неактивными аккаунтами.
 */
@Service
@RequiredArgsConstructor
public class InactiveUserCleanupService {
	private final UserRepository userRepository;

	/**
	 * Метод активируется каждую минуту и проверяет неактивные аккаунты.
	 * В случае, если аккаунт неактивен в течение 10 минут, то удаляет его с БД.
	 */
	@Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
	public void checkAndRemoveInactiveUsers() {
		Timestamp cutoffTime = Timestamp.from(
				Instant.now().minus(10, ChronoUnit.MINUTES)
		);

		userRepository
				.deleteAll(userRepository.findAllByRegistrationAt(cutoffTime));
	}
}