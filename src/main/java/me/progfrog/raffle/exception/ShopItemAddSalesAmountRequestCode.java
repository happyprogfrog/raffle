package me.progfrog.raffle.exception;

public enum ShopItemAddSalesAmountRequestCode {

    SUCCESS(1),
    INVALID_SALES_AMOUNT(2);

    ShopItemAddSalesAmountRequestCode(int code) {}

    public static ShopItemAddSalesAmountRequestCode find(String code) {
        int codeVal = Integer.parseInt(code);
        return switch (codeVal) {
            case 1 -> SUCCESS;
            case 2 -> INVALID_SALES_AMOUNT;
            default -> throw new IllegalArgumentException("존재하지 않는 코드입니다. %s".formatted(code));
        };
    }

    public static void checkRequestResult(ShopItemAddSalesAmountRequestCode code) {
        switch (code) {
            case INVALID_SALES_AMOUNT -> throw new ShopItemRaffleException(ErrorCode.INVALID_SALES_AMOUNT, "아이템이 모두 소진되었습니다.");
        }
    }
}
