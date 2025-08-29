package com.taekang.employeeservletapi.entity.employee;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Level {
  STAFF(1),
  ASSOCIATE(2),
  SENIORMANAGER(3),
  OFFICEMANAGER(4),
  MANAGER(5),
  ADMINISTRATOR(6),;

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
