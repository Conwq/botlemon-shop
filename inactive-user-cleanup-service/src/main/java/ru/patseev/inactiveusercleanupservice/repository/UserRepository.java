package ru.patseev.inactiveusercleanupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.patseev.inactiveusercleanupservice.domain.UserEntity;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	@Query("FROM UserEntity WHERE registrationAt < :cutoffTime AND enabled = false")
	List<UserEntity> findAllByRegistrationAt(@Param("cutoffTime") Timestamp cutoffTime);
}