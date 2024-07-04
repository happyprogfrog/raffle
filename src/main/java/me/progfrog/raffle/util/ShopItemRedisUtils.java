package me.progfrog.raffle.util;

public class ShopItemRedisUtils {

    public static String getShopItemSalesAmountKey(long shopItemId) {
        return "shopItem:raffle:%s:salesAmount".formatted(shopItemId);
    }
}
