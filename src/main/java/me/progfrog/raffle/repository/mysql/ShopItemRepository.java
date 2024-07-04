package me.progfrog.raffle.repository.mysql;

import me.progfrog.raffle.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
}
