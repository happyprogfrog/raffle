package me.progfrog.raffle.service;

import lombok.RequiredArgsConstructor;
import me.progfrog.raffle.exception.ErrorCode;
import me.progfrog.raffle.exception.ShopItemRaffleException;
import me.progfrog.raffle.model.ShopItem;
import me.progfrog.raffle.model.UserGiftCard;
import me.progfrog.raffle.model.UserItem;
import me.progfrog.raffle.repository.mysql.UserGiftCardRepository;
import me.progfrog.raffle.repository.mysql.UserItemRepository;
import me.progfrog.raffle.repository.redis.RedisRepository;
import me.progfrog.raffle.repository.redis.dto.ShopItemRedisEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class ShopItemRaffleService {

    private final UserGiftCardRepository userGiftCardRepository;
    private final UserItemRepository userItemRepository;
    private final RedisRepository redisRepository;

    private final ShopItemService shopItemService;
    private final ShopItemCacheService shopItemCacheService;

    private static final int RANDOM_RANGE = 10000;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Transactional
    public boolean raffleV1(final long userId, final long shopItemId) {
        ShopItem shopItem = shopItemService.findShopItem(shopItemId);
        checkAvailableRaffle(userId, shopItem.getNeedGiftCardCount());

        boolean raffleResult = isSuccessRaffle(shopItem.getProbability());
        if (raffleResult) {
            redisRepository.addSalesAmountRequest(shopItemId, shopItem.getMaxSalesAmount());
            minusUserGiftCard(userId, shopItem.getNeedGiftCardCount());
            addUserItem(userId, shopItem.getItemCode(), shopItem.getIssuedItemCount());
        }

        return raffleResult;
    }

    @Transactional
    public boolean raffleV2(final long userId, final long shopItemId) {
        ShopItemRedisEntity shopItem = shopItemCacheService.getShopItemLocalCache(shopItemId);
        checkAvailableRaffle(userId, shopItem.needGiftCardCount());

        boolean raffleResult = isSuccessRaffle(shopItem.probability());
        if (raffleResult) {
            redisRepository.addSalesAmountRequest(shopItemId, shopItem.maxSalesAmount());
            minusUserGiftCard(userId, shopItem.needGiftCardCount());
            addUserItem(userId, shopItem.itemCode(), shopItem.issuedItemCount());
        }

        return raffleResult;
    }

    private boolean isSuccessRaffle(final int probability) {
        return random.nextInt(RANDOM_RANGE) < probability;
    }

    private void checkAvailableRaffle(final long userId, final int needGiftCardCount) {
        int userGiftCardCount = getUserGiftCardCount(userId);
        if (userGiftCardCount < needGiftCardCount) {
            throw new ShopItemRaffleException(ErrorCode.INVALID_GIFT_CARD_COUNT,
                    "보유한 상품권이 부족합니다. user: %s, need: %s".formatted(userGiftCardCount, needGiftCardCount));
        }
    }

    private int getUserGiftCardCount(final long userId) {
        Optional<UserGiftCard> userGiftCardOptional = userGiftCardRepository.findByUserId(userId);
        return userGiftCardOptional.map(UserGiftCard::getCount).orElse(0);
    }

    private UserGiftCard findUserGiftCard(final long userId) {
        return userGiftCardRepository.findByUserId(userId)
                .orElseThrow(() -> new ShopItemRaffleException(ErrorCode.INVALID_USER,
                        "상품권 정보가 존재하지 않습니다. user_id: %s".formatted(userId)));
    }

    private void minusUserGiftCard(final long userId, final int usedCount) {
        UserGiftCard userGiftCard = findUserGiftCard(userId);
        userGiftCard.minusCount(usedCount);
    }

    private void addUserItem(final long userId, final String itemCode, final int issuedItemCount) {
        Optional<UserItem> userItemOptional = userItemRepository.findByUserIdAndItemCode(userId, itemCode);
        if (userItemOptional.isPresent()) {
            UserItem userItem = userItemOptional.get();
            userItem.addCount(issuedItemCount);
        } else {
            UserItem userItem = new UserItem(userId, itemCode, issuedItemCount);
            userItemRepository.save(userItem);
        }
    }
}
