package me.progfrog.raffle.repository.mysql;

import jakarta.persistence.LockModeType;
import me.progfrog.raffle.model.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserItem> findByUserIdAndItemCode(Long userId, String itemCode);
}
