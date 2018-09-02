package com.wx.somecode;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;

/**
 * @author xinquan.huangxq
 */
public class ConverterMain {

    public static void main(String[] args) {
        GenericConversionService genericConversionService = new GenericConversionService();
        genericConversionService.addConverter(new  Converter<String, Integer>() {
            @Nullable
            @Override
            public Integer convert(String source) {
                return Integer.parseInt(source);
            }
        });
        genericConversionService.addConverter(new Converter<String, Double>() {
            @Nullable
            @Override
            public Double convert(String source) {
                return Double.parseDouble(source) * 2;
            }
        });

        System.out.println(genericConversionService.convert("123", Integer.class));
        System.out.println(genericConversionService.convert("123", Double.class));
    }
}
