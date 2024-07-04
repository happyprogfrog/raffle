package me.progfrog.raffle.service;

import me.progfrog.raffle.TestConfig;
import me.progfrog.raffle.exception.ErrorCode;
import me.progfrog.raffle.exception.ShopItemRaffleException;
import me.progfrog.raffle.model.ShopItem;
import me.progfrog.raffle.model.UserGiftCard;
import me.progfrog.raffle.repository.mysql.ShopItemRepository;
import me.progfrog.raffle.repository.mysql.UserGiftCardRepository;
import me.progfrog.raffle.repository.mysql.UserItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Objects;

import static me.progfrog.raffle.util.ShopItemRedisUtils.getShopItemSalesAmountKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShopItemRaffleServiceV1Test extends TestConfig {

    @Autowired
    ShopItemRaffleService shopItemRaffleService;

    @Autowired
    ShopItemRepository shopItemRepository;

    @Autowired
    UserGiftCardRepository userGiftCardRepository;

    @Autowired
    UserItemRepository userItemRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clean() {
        Collection<String> redisKeys = redisTemplate.keys("*");
        redisTemplate.delete(redisKeys);

        shopItemRepository.deleteAllInBatch();
        userGiftCardRepository.deleteAllInBatch();
        userItemRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상품이 존재하지 않으면 예외 발생")
    void checkNotExistShopItem() {
        // given
        long userId = 1;
        long shopItemId = 1;

        // when & then
        ShopItemRaffleException ex = assertThrows(ShopItemRaffleException.class, () ->
                shopItemRaffleService.raffleV1(userId, shopItemId));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_ITEM);
    }

    @Test
    @DisplayName("유저가 보유한 상품권이 부족하면 예외 발생 1")
    void checkNotEnoughUserGiftCardCount_1() {
        // given
        long userId = 1;
        ShopItem shopItem = new ShopItem("item1", 10, 100, 100, 500);
        shopItemRepository.save(shopItem);

        // when & then
        ShopItemRaffleException ex = assertThrows(ShopItemRaffleException.class, () ->
                shopItemRaffleService.raffleV1(userId, shopItem.getId()));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_GIFT_CARD_COUNT);
    }

    @Test
    @DisplayName("유저가 보유한 상품권이 부족하면 예외 발생 2")
    void checkNotEnoughUserGiftCardCount_2() {
        // given
        long userId = 1;
        ShopItem shopItem = new ShopItem("item1", 10, 100, 100, 5000);
        shopItemRepository.save(shopItem);

        UserGiftCard userGiftCard = new UserGiftCard(userId, 99);
        userGiftCardRepository.save(userGiftCard);

        // when & then
        ShopItemRaffleException ex = assertThrows(ShopItemRaffleException.class, () ->
                shopItemRaffleService.raffleV1(userId, shopItem.getId()));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_GIFT_CARD_COUNT);
    }

    @Test
    @DisplayName("상품이 모두 소진된 경우 예외 발생")
    void checkMaxSalesAmount() {
        // given
        long userId = 1;
        ShopItem shopItem = new ShopItem("item1", 10, 100, 100, 10000);
        shopItemRepository.save(shopItem);

        UserGiftCard userGiftCard = new UserGiftCard(userId, 200);
        userGiftCardRepository.save(userGiftCard);

        redisTemplate.opsForValue().set(getShopItemSalesAmountKey(shopItem.getId()),
                String.valueOf(shopItem.getMaxSalesAmount()));

        // when & then
        ShopItemRaffleException ex = assertThrows(ShopItemRaffleException.class, () ->
                shopItemRaffleService.raffleV1(userId, shopItem.getId()));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_SALES_AMOUNT);
    }

    @Test
    @DisplayName("예외가 없으면 응모 진행 - 당첨")
    void doRaffle_1() {
        // given
        long userId = 1;
        ShopItem shopItem = new ShopItem("item1", 10, 100, 100, 10000);
        shopItemRepository.save(shopItem);

        UserGiftCard userGiftCard = new UserGiftCard(userId, 200);
        userGiftCardRepository.save(userGiftCard);

        redisTemplate.opsForValue().set(getShopItemSalesAmountKey(shopItem.getId()),
                String.valueOf(shopItem.getMaxSalesAmount() - 10));

        // when
        boolean resultRaffle = shopItemRaffleService.raffleV1(userId, shopItem.getId());

        // then
        String valStr = redisTemplate.opsForValue().get(getShopItemSalesAmountKey(shopItem.getId()));
        assertThat(resultRaffle).isTrue();
        assertThat(Integer.parseInt(Objects.requireNonNull(valStr))).isEqualTo(shopItem.getMaxSalesAmount() - 10 + 1);
        assertThat(userGiftCard.getCount()).isEqualTo(200 - 100);
        assertThat((long) userItemRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예외가 없으면 응모 진행 - 미당첨")
    void doRaffle() {
        // given
        long userId = 1;
        ShopItem shopItem = new ShopItem("item1", 10, 100, 100, 0);
        shopItemRepository.save(shopItem);

        UserGiftCard userGiftCard = new UserGiftCard(userId, 200);
        userGiftCardRepository.save(userGiftCard);

        redisTemplate.opsForValue().set(getShopItemSalesAmountKey(shopItem.getId()),
                String.valueOf(shopItem.getMaxSalesAmount() - 10));

        // when
        boolean resultRaffle = shopItemRaffleService.raffleV1(userId, shopItem.getId());

        // then
        String valStr = redisTemplate.opsForValue().get(getShopItemSalesAmountKey(shopItem.getId()));
        assertThat(resultRaffle).isFalse();
        assertThat(Integer.parseInt(Objects.requireNonNull(valStr))).isEqualTo(shopItem.getMaxSalesAmount() - 10);
    }
}