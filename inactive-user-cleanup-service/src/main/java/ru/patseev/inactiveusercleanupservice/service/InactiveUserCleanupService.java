package ru.patseev.inactiveusercleanupservice.service;

/**
 * Сервис для работы с неактивными аккаунтами.
 */
public interface InactiveUserCleanupService {

	/**
	 * Метод активируется каждую минуту и проверяет неактивные аккаунты.
	 * В случае, если аккаунт неактивен в течение 10 минут, то удаляет его с БД.
	 */
	void checkAndRemoveInactiveUsers();
}