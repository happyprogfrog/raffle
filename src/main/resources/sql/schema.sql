CREATE TABLE `shop_item` (
	`shop_item_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`item_code` VARCHAR(20) NOT NULL COMMENT '아이템 ID' COLLATE 'utf8_general_ci',
	`issued_item_count` INT(11) NOT NULL COMMENT '1회당 아이템 지급 개수',
	`need_gift_card_count` INT(11) NOT NULL COMMENT '필요 상품권 개수',
	`max_sales_amount` INT(11) NOT NULL COMMENT '최대 판매 수량',
	`probability` INT(11) NOT NULL DEFAULT '10000' COMMENT '응모 시 당첨 확률 (만분율)',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '행이 추가된 시간',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '행이 수정된 시간',
	PRIMARY KEY (`shop_item_id`) USING BTREE
)
COMMENT='상점 상품'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

CREATE TABLE `user_gift_card` (
	`user_gift_card_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT(20) NOT NULL,
	`count` INT(11) NOT NULL COMMENT '보유한 상품권 개수',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '행이 추가된 시간',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '행이 수정된 시간',
	PRIMARY KEY (`user_gift_card_id`) USING BTREE,
	UNIQUE INDEX `unique_user_id` (`user_id`) USING BTREE
)
COMMENT='상점 유저 재화 보유량'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

CREATE TABLE `user_item` (
    `user_item_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `item_code` VARCHAR(20) NOT NULL COMMENT '아이템 ID' COLLATE 'utf8_general_ci',
    `item_count` INT(11) NOT NULL COMMENT '아이템 개수',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '행이 추가된 시간',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '행이 수정된 시간',
	PRIMARY KEY (`user_item_id`) USING BTREE,
	INDEX `idx_user_id` (`user_id`) USING BTREE
)
COMMENT='유저 보유 아이템'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;