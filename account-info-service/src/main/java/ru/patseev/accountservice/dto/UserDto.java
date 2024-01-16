package ru.patseev.accountservice.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) представляющий пользователя.
 *
 * <p>Этот класс предназначен для передачи данных о пользователе между различными частями системы.
 * Он содержит основные атрибуты пользователя, такие как идентификатор, электронная почта, имя пользователя,
 * пароль и другие свойства. Используется в контексте взаимодействия с внешними системами, передачи данных
 * через сеть или сохранения в хранилище данных.</p>
 *
 * <p>Класс создан как "record" (запись), предоставляя компактный и неизменяемый способ представления данных.</p>
 *
 * @param id             Идентификатор пользователя.
 * @param email          Электронная почта пользователя.
 * @param username       Имя пользователя.
 * @param password       Пароль пользователя.
 * @param firstName      Имя пользователя.
 * @param lastName       Фамилия пользователя.
 * @param role           Роль пользователя.
 * @param activationCode Код активации учетной записи пользователя.
 * @param enabled        Показатель активности учетной записи.
 */
public record UserDto(Integer id,
					  String email,
					  String username,
					  String password,
					  String firstName,
					  String lastName,
					  String registrationAt
//					  UserRoles role
//					  String activationCode,
//					  Boolean enabled
) {
}
