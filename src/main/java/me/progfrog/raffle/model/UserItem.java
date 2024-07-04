package me.progfrog.raffle.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_item_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String itemCode;

    @Column(nullable = false)
    private int itemCount;

    public UserItem(Long userId, String itemCode, int itemCount) {
        this.userId = userId;
        this.itemCode = itemCode;
        this.itemCount = itemCount;
    }

    public void addCount(final int issuedItemCount) {
        if (issuedItemCount <= 0) {
            throw new IllegalArgumentException("지급하는 수량이 음수입니다. %s".formatted(issuedItemCount));
        }

        this.itemCount += issuedItemCount;
    }
}
