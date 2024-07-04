package me.progfrog.raffle.repository.redis;

import lombok.RequiredArgsConstructor;
import me.progfrog.raffle.exception.ShopItemAddSalesAmountRequestCode;
import me.progfrog.raffle.util.ShopItemRedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<String> salesAmountScript = checkAndAddSalesAmountRequestScript();

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void addSalesAmountRequest(long shopItemId, int maxSalesAmount) {
        String key = ShopItemRedisUtils.getShopItemSalesAmountKey(shopItemId);

        String code = redisTemplate.execute(
                salesAmountScript,
                List.of(key),
                String.valueOf(maxSalesAmount)
        );

        ShopItemAddSalesAmountRequestCode.checkRequestResult(
                ShopItemAddSalesAmountRequestCode.find(code)
        );
    }

    private RedisScript<String> checkAndAddSalesAmountRequestScript() {
        String script = """
                local currentSalesAmount = redis.call('GET', KEYS[1])
                if currentSalesAmount == false then
                    currentSalesAmount = 0
                else
                    currentSalesAmount = tonumber(currentSalesAmount)
                end

                if tonumber(ARGV[1]) > currentSalesAmount then
                    redis.call('INCR', KEYS[1])
                    return '1'
                end
                
                return '2'
                """;
        return RedisScript.of(script, String.class);
    }
}
