package com.taekang.employeeservletapi.api;

import com.taekang.employeeservletapi.DTO.BalanceResponseDTO;
import com.taekang.employeeservletapi.entity.user.ChainType;
import com.taekang.employeeservletapi.error.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class CryptoBalanceAPI {

  @Value("${api.node.crypto.tracker.uri}")
  private String uri;

  private final String VALIDATION_PATH = "/wallet/get";

  private final RestTemplate restTemplate;

  @Autowired
  public CryptoBalanceAPI(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public BalanceResponseDTO getBalance(ChainType chainType, String cryptoWallet) {
    URI requestUri =
        UriComponentsBuilder.fromUriString(uri)
            .path(VALIDATION_PATH)
            .queryParam("chain", chainType.name())
            .queryParam("address", cryptoWallet)
            .encode()
            .build()
            .toUri();

    try {
      ResponseEntity<BalanceResponseDTO> responseEntity =
          restTemplate.getForEntity(requestUri, BalanceResponseDTO.class); // [수정] requestUri 사용

      // 1. 응답 본문(body)이 null인지 확인합니다.
      if (responseEntity.getBody() == null) {
        log.warn("[TronWalletValidation] Response body is null for URI: {}", requestUri);
        throw new AccountNotFoundException();
      }

      log.info("[TronWalletValidation] Response: {}", responseEntity.getBody());
      return responseEntity.getBody();

    } catch (RestClientException e) {
      // 2. RestTemplate에서 발생하는 모든 예외(4xx, 5xx, 타임아웃 등)를 처리합니다.
      log.error("[TronWalletValidation] API call failed for URI: {}", requestUri, e);
      throw new AccountNotFoundException();
    } catch (Exception e) {
      throw new AccountNotFoundException();
    }
  }
}
