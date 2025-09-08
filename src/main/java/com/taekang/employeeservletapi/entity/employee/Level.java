package com.taekang.employeeservletapi.entity.employee;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Level {
  STAFF(1),
  MANAGER(2),
  ADMINISTRATOR(3),
  DEVELOPER(4),
  ;

  private final int rank;

  Level(int rank) {
    this.rank = rank;
  }

  // rank 숫자로 Level enum을 찾는 메소드 추가
  public static Level fromRank(int rank) {
    return Arrays.stream(Level.values())
        .filter(level -> level.getRank() == rank)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid Level rank: " + rank));
  }
}
