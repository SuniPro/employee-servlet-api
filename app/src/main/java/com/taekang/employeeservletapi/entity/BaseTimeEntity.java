package com.taekang.employeeservletapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // 이 클래스를 상속하는 엔티티는 아래 필드들을 컬럼으로 인식합니다.
@EntityListeners(AuditingEntityListener.class) // 엔티티의 변경을 감지하여 자동으로 값을 넣어줍니다.
public abstract class BaseTimeEntity {

  @CreatedDate // 엔티티가 생성될 때의 시간이 자동으로 저장됩니다.
  @Column(name = "insert_date_time", updatable = false) // insert_date_time으로 컬럼 생성, 업데이트 불가
  private LocalDateTime insertDateTime;

  @LastModifiedDate // 엔티티가 수정될 때의 시간이 자동으로 저장됩니다.
  @Column(name = "update_date_time")
  private LocalDateTime updateDateTime;
}
