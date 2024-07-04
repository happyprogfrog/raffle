INSERT INTO `shop_item` (
    `item_code`, `issued_item_count`, `need_gift_card_count`, `max_sales_amount`, `probability`, `created_at`, `updated_at`
) VALUES
('ITEM001', 10, 2, 100, 10000, NOW(), NOW()),
('ITEM002', 5, 1, 200, 9500, NOW(), NOW()),
('ITEM003', 20, 3, 50, 9000, NOW(), NOW()),
('ITEM004', 15, 2, 150, 8500, NOW(), NOW()),
('ITEM005', 25, 4, 120, 8000, NOW(), NOW()),
('ITEM006', 30, 5, 80, 7500, NOW(), NOW()),
('ITEM007', 10, 1, 300, 7000, NOW(), NOW()),
('ITEM008', 50, 6, 60, 6500, NOW(), NOW()),
('ITEM009', 5, 3, 250, 6000, NOW(), NOW()),
('ITEM010', 40, 7, 90, 5500, NOW(), NOW());

INSERT INTO `user_gift_card` (
    `user_id`, `count`, `created_at`, `updated_at`
) VALUES
(1001, 5, NOW(), NOW()),
(1002, 3, NOW(), NOW()),
(1003, 10, NOW(), NOW()),
(1004, 8, NOW(), NOW()),
(1005, 15, NOW(), NOW()),
(1006, 20, NOW(), NOW()),
(1007, 1, NOW(), NOW()),
(1008, 7, NOW(), NOW()),
(1009, 12, NOW(), NOW()),
(1010, 4, NOW(), NOW());
