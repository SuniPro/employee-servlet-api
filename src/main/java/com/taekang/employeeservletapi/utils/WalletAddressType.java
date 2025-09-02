package com.taekang.employeeservletapi.utils;

import com.taekang.employeeservletapi.entity.user.ChainType;
import com.taekang.employeeservletapi.error.IsNotSupportWalletTypeException;
import com.taekang.employeeservletapi.service.CryptoValidationService;

public enum WalletAddressType {
    ETH {
        @Override
        public boolean isInvalid(CryptoValidationService cryptoValidation, String address) {
            return cryptoValidation.ethWalletValidation(address);
        }
    },
    TRON {
        @Override
        public boolean isInvalid(CryptoValidationService cryptoValidation, String address) {
            return cryptoValidation.tronWalletValidation(address);
        }
    },

    BTC {
        @Override
        public boolean isInvalid(CryptoValidationService cryptoValidation, String address) {
            return cryptoValidation.btcWalletValidation(address);
        }
    },

    UNKNOWN {
        @Override
        public boolean isInvalid(CryptoValidationService cryptoValidation, String address) {
            // 알 수 없는 타입은 항상 유효하지 않음
            return true;
        }
    };

    public ChainType toChainType() {
        return switch (this) {
            case ETH -> ChainType.ETH;
            case TRON -> ChainType.TRON;
            case BTC -> ChainType.BTC;
            default -> throw new IsNotSupportWalletTypeException();
        };
    }

    /**
     * 각 지갑 타입에 맞는 검증 로직을 실행하여 주소가 '유효하지 않은지' 확인합니다.
     *
     * @param cryptoValidation 검증 로직을 담고 있는 서비스
     * @param address 검증할 지갑 주소
     * @return 유효하지 않으면 true, 유효하면 false
     */
    public abstract boolean isInvalid(CryptoValidationService cryptoValidation, String address);

    /**
     * 주어진 지갑 주소 문자열을 분석하여 어떤 형식인지 판별합니다.
     *
     * @param address 검사할 지갑 주소
     * @return 판별된 지갑 주소 타입 (ETHEREUM, TRON, UNKNOWN)
     */
    public static WalletAddressType of(String address) {
        // 주소가 null이거나 비어있으면 UNKNOWN
        if (address == null || address.trim().isEmpty()) {
            return UNKNOWN;
        }

        // 1. 이더리움(ETH) 주소 형식 검사
        // - '0x'로 시작
        // - 이후 40자의 16진수 문자(0-9, a-f, A-F)
        // - 총 길이 42자
        if (address.matches("^0x[a-fA-F0-9]{40}$")) {
            return ETH;
        }

        // 2. 트론(TRON) 주소 형식 검사
        // - 'T'로 시작
        // - 총 길이 34자
        if (address.startsWith("T") && address.length() == 34) {
            return TRON;
        }

        // 3. 비트코인(BTC) 주소 형식 검사 (3가지 타입 모두 포함)
        // - 1로 시작하는 Legacy 주소
        // - 3으로 시작하는 P2SH 주소
        // - bc1으로 시작하는 Bech32 주소
        if (address.matches("^(bc1|[13])[a-zA-HJ-NP-Z0-9]{25,39}$")) {
            return BTC;
        }

        // 3. 위 두 형식에 해당하지 않는 경우
        return UNKNOWN;
    }
}

