package il.ac.afeka.cloud.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import il.ac.afeka.cloud.enums.SortOrderEnum;

@Component
public class StringToSortOrderEnumConverter implements Converter<String, SortOrderEnum> {

	@Override
	public SortOrderEnum convert(String source) {
		try {
            return SortOrderEnum.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
	}

}
