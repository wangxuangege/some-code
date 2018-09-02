package com.wx.somecode;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.number.PercentStyleFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author xinquan.huangxq
 */
public final class NumberFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<NumberFormat> {

    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[]{
                Short.class, Integer.class, Long.class, Float.class,
                Double.class, BigDecimal.class, BigInteger.class}));
    }

    public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType) {
        if (!annotation.pattern().isEmpty()) {
            return new NumberStyleFormatter(annotation.pattern());
        } else {
            NumberFormat.Style style = annotation.style();
            if (style == NumberFormat.Style.PERCENT) {
                return new PercentStyleFormatter();
            } else if (style == NumberFormat.Style.CURRENCY) {
                return new CurrencyStyleFormatter();
            } else {
                return new NumberStyleFormatter();
            }
        }
    }
}
