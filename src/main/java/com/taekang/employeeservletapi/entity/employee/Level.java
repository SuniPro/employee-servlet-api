package com.taekang.employeeservletapi.entity.employee;

public enum Level {
  STAFF(1),
  ASSOCIATE(2),
  SENIORMANAGER(3),
  OFFICEMANAGER(4),
  MANAGER(5),
  CTO(6),
  CDO(7),
  CIO(8),
  CFO(9),
  COO(10),
  CEO(11);

  private final int rank;

  Level(int rank) {
    this.rank = rank;
  }

  public int getRank() {
    return rank;
  }
}
