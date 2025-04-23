package com.taekang.employeeservletapi.tools.converter;

import com.taekang.employeeservletapi.entity.employee.WorkMenu;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class WorkMenuListConverter implements AttributeConverter<List<WorkMenu>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<WorkMenu> attribute) {
        return attribute == null ? "" :
                attribute.stream().map(Enum::name).collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<WorkMenu> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return new ArrayList<>();
        return Arrays.stream(dbData.split(DELIMITER))
                .map(WorkMenu::valueOf)
                .toList();
    }
}
