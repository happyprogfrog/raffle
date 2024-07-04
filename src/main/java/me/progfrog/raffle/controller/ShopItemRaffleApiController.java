package me.progfrog.raffle.controller;

import lombok.RequiredArgsConstructor;
import me.progfrog.raffle.dto.ShopItemRaffleRequestDto;
import me.progfrog.raffle.dto.ShopItemRaffleResponseDto;
import me.progfrog.raffle.service.ShopItemRaffleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShopItemRaffleApiController {

    private final ShopItemRaffleService shopItemRaffleService;

    @PostMapping("/v1/raffle")
    public ResponseEntity<ShopItemRaffleResponseDto> raffleV1(@RequestBody ShopItemRaffleRequestDto requestDto) {
        boolean resultRaffle = shopItemRaffleService.raffleV1(requestDto.userId(), requestDto.shopItemId());
        return ResponseEntity.ok(new ShopItemRaffleResponseDto(resultRaffle));
    }

    @PostMapping("/v2/raffle")
    public ResponseEntity<ShopItemRaffleResponseDto> raffleV2(@RequestBody ShopItemRaffleRequestDto requestDto) {
        boolean resultRaffle = shopItemRaffleService.raffleV2(requestDto.userId(), requestDto.shopItemId());
        return ResponseEntity.ok(new ShopItemRaffleResponseDto(resultRaffle));
    }
}
