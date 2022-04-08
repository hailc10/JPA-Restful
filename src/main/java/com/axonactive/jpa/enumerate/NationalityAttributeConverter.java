package com.axonactive.jpa.enumerate;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
@Converter(autoApply = true)
public class NationalityAttributeConverter implements AttributeConverter<Nationality, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Nationality nationality) {
        return (nationality != null)?
                nationality.getValue() : null;
    }

    @Override
    public Nationality convertToEntityAttribute(Integer integer) {
        return Arrays.stream(Nationality.values()).filter(nationality -> nationality.getValue() == integer).findFirst().get();
    }
}
