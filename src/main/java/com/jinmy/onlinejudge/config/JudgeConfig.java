package com.jinmy.onlinejudge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "onlinejudge.switches")
public class JudgeConfig {
    private Boolean executing;
    private Boolean submit;
    private Boolean enterSystem;
    private Boolean sslEnabled;
}
