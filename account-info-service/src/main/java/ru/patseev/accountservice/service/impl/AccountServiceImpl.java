package ru.patseev.accountservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.patseev.accountservice.client.UserServiceClient;
import ru.patseev.accountservice.domain.AccountDetailsEntity;
import ru.patseev.accountservice.dto.AccountDto;
import ru.patseev.accountservice.dto.UserDto;
import ru.patseev.accountservice.repository.AccountDetailsRepository;
import ru.patseev.accountservice.service.AccountService;
import ru.patseev.accountservice.service.exception.UserNotFoundException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final UserServiceClient userServiceClient;
	private final AccountDetailsRepository accountDetailsRepository;

	@Override
	@Transactional(readOnly = true)
	public AccountDto getUserAccountDetails(String username) {
		UserDto userDto = userServiceClient
				.sendRequestToReceiveUserCredentials(username)
				.orElseThrow(() -> new UserNotFoundException("User nor found"));

		AccountDetailsEntity accountDetailsEntity = accountDetailsRepository.
				findByUserId(userDto.id())
				.orElseThrow();

		return this.createAccountDto(userDto, accountDetailsEntity);
	}

	@Override
	@Transactional
	public void addAccountDetails(int userId) {
		AccountDetailsEntity accountDetails = this.createAccountDetailsEntity(userId);
		accountDetailsRepository.save(accountDetails);
	}

	/*
	 * Создает новый объект AccountDetailsEntity.
	 */
	private AccountDetailsEntity createAccountDetailsEntity(int userId) {
		return AccountDetailsEntity.builder()
				.lastLoginDate(Timestamp.from(Instant.now()))
				.totalPurchaseAmount(BigDecimal.ZERO)
				.bonusPoints(0)
				.userId(userId)
				.build();
	}

	/*
	 * Создает новый объект AccountDto.
	 */
	private AccountDto createAccountDto(UserDto userDto, AccountDetailsEntity accountDetailsEntity) {
		return new AccountDto(
				userDto.email(),
				userDto.username(),
				userDto.firstName(),
				userDto.lastName(),
				userDto.registrationAt(),
				accountDetailsEntity.getLastLoginDate().toString(),
				accountDetailsEntity.getTotalPurchaseAmount(),
				accountDetailsEntity.getBonusPoints()
		);
	}
}