package com.ruoyi.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

/**
 * 跨境电商平台 交易闭环模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class RuoYiCommerceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiCommerceApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  跨境电商交易闭环模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  product · customer · order · payment · inventory · fulfillment · logistics");
    }
}
