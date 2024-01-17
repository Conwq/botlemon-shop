package ru.patseev.emailsenderservice.service;

import org.springframework.mail.MailException;
import ru.patseev.emailsenderservice.dto.EmailSendRequest;

/**
 * Сервис для отправки электронных писем.
 */
public interface EmailSenderService {

	/**
	 * Отправляет электронное письмо с активационным кодом на указанный адрес пользователя.
	 *
	 * @param request объект с данными для отправки письма, включая адрес пользователя и активационный код
	 * @throws MailException если произошла ошибка при отправке письма
	 */
	void sendEmailForActivateAccountToUserEmail(EmailSendRequest request);
}