package ru.patseev.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.reviewservice.domain.ReviewEntity;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

	List<ReviewEntity> findAllByItemEntityId(int itemId);
}
