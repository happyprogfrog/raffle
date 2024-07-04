package me.progfrog.raffle.service;

import lombok.RequiredArgsConstructor;
import me.progfrog.raffle.exception.ErrorCode;
import me.progfrog.raffle.exception.ShopItemRaffleException;
import me.progfrog.raffle.model.ShopItem;
import me.progfrog.raffle.repository.mysql.ShopItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopItemService {

    private final ShopItemRepository shopItemRepository;

    @Transactional(readOnly = true)
    public ShopItem findShopItem(final long shopItemId) {
        return shopItemRepository.findById(shopItemId)
                .orElseThrow(() -> new ShopItemRaffleException(ErrorCode.INVALID_ITEM,
                        "상품이 존재하지 않습니다. shop_item_id: %s".formatted(shopItemId)));
    }
}
