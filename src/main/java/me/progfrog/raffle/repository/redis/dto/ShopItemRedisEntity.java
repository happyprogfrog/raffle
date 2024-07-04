package me.progfrog.raffle.repository.redis.dto;

import me.progfrog.raffle.model.ShopItem;

public record ShopItemRedisEntity(
        Long id,
        String itemCode,
        int issuedItemCount,
        int needGiftCardCount,
        int maxSalesAmount,
        int probability
){
    public ShopItemRedisEntity(ShopItem shopItem) {
        this(
                shopItem.getId(),
                shopItem.getItemCode(),
                shopItem.getIssuedItemCount(),
                shopItem.getNeedGiftCardCount(),
                shopItem.getMaxSalesAmount(),
                shopItem.getProbability()
        );
    }
}
