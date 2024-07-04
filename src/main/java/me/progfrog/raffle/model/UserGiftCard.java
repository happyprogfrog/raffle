package me.progfrog.raffle.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserGiftCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_gift_card_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int count;

    public UserGiftCard(Long userId, int count) {
        this.userId = userId;
        this.count = count;
    }

    public void minusCount(final int usedCount) {
        if (usedCount <= 0) {
            throw new IllegalArgumentException("차감하는 수량이 음수입니다. %s".formatted(usedCount));
        }

        if (count - usedCount < 0) {
            throw new IllegalArgumentException("보유 수량보다 더 많은 수량을 차감합니다. curr: %s, used: %s".formatted(usedCount, usedCount));
        }

        this.count -= usedCount;
    }
}
