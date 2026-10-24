package com.ruoyi.commerce.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 业务单号生成工具。
 *
 * @author ruoyi
 */
public class CommerceNoUtils
{
    private CommerceNoUtils()
    {
    }

    public static String orderNo()
    {
        return build("NC-");
    }

    public static String paymentNo()
    {
        return build("PAY");
    }

    public static String refundNo()
    {
        return build("RF-");
    }

    public static String fulfillmentNo()
    {
        return build("FUL");
    }

    public static String shipmentNo()
    {
        return build("SHP");
    }

    public static String customerNo()
    {
        return build("CUS");
    }

    public static String settlementNo()
    {
        return build("STL");
    }

    private static String build(String prefix)
    {
        String time = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return prefix + time + random;
    }
}
