package il.ac.afeka.cloud.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import il.ac.afeka.cloud.enums.FilterTypeEnum;

@Component
public class StringToFilterTypeEnumConverter implements Converter<String, FilterTypeEnum> {

	@Override
	public FilterTypeEnum convert(String source) {
		try {
            return FilterTypeEnum.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
	}

}
