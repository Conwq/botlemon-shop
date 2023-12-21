package ru.patseev.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.reviewservice.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
