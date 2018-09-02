package com.wx.somecode;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @author xinquan.huangxq
 */
public class FormatterMain {

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Setter
    @Getter
    private BigDecimal decimal;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Setter
    @Getter
    private Date date;

    public static void main(String[] args) throws ParseException {
        DateFormatter df = new DateFormatter("yyyy-MM-dd");
        System.out.println(df.print(new Date(), Locale.CHINA));
        System.out.println(df.parse("2018-09-27", Locale.ENGLISH));

        FormatterMain fm = new FormatterMain();
        fm.setDate(new Date());
        fm.setDecimal(BigDecimal.TEN);
        System.out.println(JSON.toJSONString(fm));
    }
}
