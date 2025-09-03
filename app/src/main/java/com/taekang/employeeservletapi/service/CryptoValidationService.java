package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.utils.WalletAddressType;
import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class CryptoValidationService {

  @Value("${api.node.crypto.tracker.uri}")
  private String baseUri;

  private final RestTemplate restTemplate;

  public CryptoValidationService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public boolean tronWalletValidation(String wallet) {
    return validate(WalletAddressType.TRON, wallet);
  }

  public boolean ethWalletValidation(String wallet) {
    return validate(WalletAddressType.ETH, wallet);
  }

  public boolean btcWalletValidation(String wallet) {
    return validate(WalletAddressType.BTC, wallet);
  }

  /** 중복 로직을 하나로 통합한 private 메소드 */
  private boolean validate(WalletAddressType type, String wallet) {
    // enum 이름을 소문자로 바꿔 경로에 사용 (e.g., ETHEREUM -> "ethereum")
    String cryptoPath = type.name().toLowerCase();

    String VALIDATION_PATH = "/validation/get/";
    URI requestUri =
        UriComponentsBuilder.fromUriString(baseUri)
            .path(VALIDATION_PATH + cryptoPath)
            .queryParam("wallet", wallet)
            .encode()
            .build()
            .toUri();

    log.info("[WalletValidation] Request URI: {}, wallet={}", requestUri, wallet);

    try {
      // [버그 수정] baseUri -> requestUri로 변경하여 올바른 주소로 요청
      ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(requestUri, Boolean.class);
      log.info("[WalletValidation] Response: {}", responseEntity.getBody());
      return Objects.requireNonNullElse(responseEntity.getBody(), false);
    } catch (Exception e) {
      // API 호출 실패 시 로그를 남기고 false를 반환하거나, 비즈니스 로직에 맞는 예외 처리
      log.error("[WalletValidation] API call failed for wallet {}: {}", wallet, e.getMessage());
      return false;
    }
  }
}
