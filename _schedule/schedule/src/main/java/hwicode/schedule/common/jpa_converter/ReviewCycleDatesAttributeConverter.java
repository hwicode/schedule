package hwicode.schedule.common.jpa_converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ReviewCycleDatesAttributeConverter implements AttributeConverter<List<Integer>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Integer> reviewCycleDates) {
        if (reviewCycleDates == null || reviewCycleDates.isEmpty()) {
            return null;
        }
        return reviewCycleDates.stream()
                .map(Object::toString)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
