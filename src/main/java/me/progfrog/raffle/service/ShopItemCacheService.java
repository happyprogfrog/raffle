package me.progfrog.raffle.service;

import lombok.RequiredArgsConstructor;
import me.progfrog.raffle.model.ShopItem;
import me.progfrog.raffle.repository.redis.dto.ShopItemRedisEntity;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopItemCacheService {

    private final ShopItemService shopItemService;

    @Cacheable(cacheNames = "shopItem", key = "#p0")
    public ShopItemRedisEntity getShopItemCache(long shopItemId) {
        ShopItem shopItem = shopItemService.findShopItem(shopItemId);
        return new ShopItemRedisEntity(shopItem);
    }

    @Cacheable(cacheNames = "shopItem", key = "#p0", cacheManager = "localCacheManager")
    public ShopItemRedisEntity getShopItemLocalCache(long shopItemId) {
        return proxy().getShopItemCache(shopItemId);
    }

    private ShopItemCacheService proxy() {
        return ((ShopItemCacheService) AopContext.currentProxy());
    }
}
