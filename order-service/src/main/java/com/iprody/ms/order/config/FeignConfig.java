package com.iprody.ms.order.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.iprody.ms.order.integration")
public class FeignConfig {
}
