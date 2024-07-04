package me.progfrog.raffle.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ShopItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_item_id")
    private Long id;

    @Column(nullable = false)
    private String itemCode;

    @Column(nullable = false)
    private int issuedItemCount;

    @Column(nullable = false)
    private int needGiftCardCount;

    @Column(nullable = false)
    private int maxSalesAmount;

    @Column(nullable = false)
    private int probability;

    public ShopItem(String itemCode, int issuedItemCount, int needGiftCardCount, int maxSalesAmount, int probability) {
        this.itemCode = itemCode;
        this.issuedItemCount = issuedItemCount;
        this.needGiftCardCount = needGiftCardCount;
        this.maxSalesAmount = maxSalesAmount;
        this.probability = probability;
    }
}
