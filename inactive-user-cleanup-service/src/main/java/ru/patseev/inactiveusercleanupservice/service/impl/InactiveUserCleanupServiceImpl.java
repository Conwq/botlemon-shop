package ru.patseev.inactiveusercleanupservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.patseev.inactiveusercleanupservice.repository.UserRepository;
import ru.patseev.inactiveusercleanupservice.service.InactiveUserCleanupService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InactiveUserCleanupServiceImpl implements InactiveUserCleanupService {
	private final UserRepository userRepository;

	@Override
	@Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
	public void checkAndRemoveInactiveUsers() {
		Timestamp cutoffTime = Timestamp.from(
				Instant.now().minus(10, ChronoUnit.MINUTES)
		);

		userRepository
				.deleteAll(userRepository.findAllByRegistrationAt(cutoffTime));
	}
}