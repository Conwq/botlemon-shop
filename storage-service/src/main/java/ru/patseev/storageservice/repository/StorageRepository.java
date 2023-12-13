package ru.patseev.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.storageservice.domain.StorageEntity;

import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity, Integer> {

	Optional<StorageEntity> findByItemId(int itemId);
}