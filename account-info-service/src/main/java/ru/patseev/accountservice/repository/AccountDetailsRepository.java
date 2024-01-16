package ru.patseev.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.accountservice.domain.AccountDetailsEntity;

import java.util.Optional;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetailsEntity, Integer> {
	Optional<AccountDetailsEntity> findByUserId(Integer userId);
}
