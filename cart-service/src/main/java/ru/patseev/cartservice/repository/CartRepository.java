package ru.patseev.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.patseev.cartservice.domain.CartEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

	Optional<CartEntity> findByUserEntityIdAndItemEntityId(int userEntityId, int itemEntityId);

	List<CartEntity> findAllByUserEntityId(int userId);
}
