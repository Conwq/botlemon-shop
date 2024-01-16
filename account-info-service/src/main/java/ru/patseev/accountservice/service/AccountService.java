package ru.patseev.accountservice.service;

import ru.patseev.accountservice.dto.AccountDto;

/**
 * Сервис отвечающий за работу с данными аккаунта пользователя.
 */
public interface AccountService {

	/**
	 * Получает данные аккаунта пользователя.
	 *
	 * @param userId Идентификатор пользователя.
	 * @return Возвращает данные аккаунта.
	 */
	AccountDto getUserAccountDetails(String userId);

	/**
	 * Добавляет данные аккаунта для пользователя.
	 *
	 * @param userId Идентификатор пользователя.
	 */
	void addAccountDetails(int userId);
}