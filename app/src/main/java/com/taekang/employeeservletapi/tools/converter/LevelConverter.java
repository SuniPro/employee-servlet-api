package com.taekang.employeeservletapi.tools.converter;

import com.taekang.employeeservletapi.entity.employee.Level;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter // 이 클래스를 JPA Converter로 사용하겠다고 선언
public class LevelConverter implements AttributeConverter<Level, Integer> {

    // Level enum을 DB에 저장할 Integer 값으로 변환
    @Override
    public Integer convertToDatabaseColumn(Level attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getRank(); // enum의 rank 숫자 값을 반환
    }

    // DB의 Integer 값을 Level enum으로 변환
    @Override
    public Level convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Level.fromRank(dbData); // rank 숫자에 맞는 enum을 찾아 반환
    }
}