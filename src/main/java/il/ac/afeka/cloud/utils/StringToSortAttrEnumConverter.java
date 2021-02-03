package il.ac.afeka.cloud.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import il.ac.afeka.cloud.enums.SortAttrEnum;

@Component
public class StringToSortAttrEnumConverter implements Converter<String, SortAttrEnum> {

	@Override
	public SortAttrEnum convert(String source) {
		try {
            return SortAttrEnum.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
	}

}
