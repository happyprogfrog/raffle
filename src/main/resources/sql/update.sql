UPDATE `user_gift_card`
SET `count` = `count` + 5,
    `updated_at` = NOW()
WHERE `user_id` = 1001;
