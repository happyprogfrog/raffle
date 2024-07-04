package me.progfrog.raffle.repository.mysql;

import jakarta.persistence.LockModeType;
import me.progfrog.raffle.model.UserGiftCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserGiftCardRepository extends JpaRepository<UserGiftCard, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserGiftCard> findByUserId(long userId);
}
