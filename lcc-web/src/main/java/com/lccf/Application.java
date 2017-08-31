
package com.lccf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lccf.config.LocaleConfiguration;
import com.lccf.config.MappingConfiguration;
import com.lccf.config.security.WebSecurityConfig;
import com.lccf.constants.LccfProperties;
import com.lccf.util.SpringUtil;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({ LccfProperties.class })
@Import({
    SpringUtil.class,
    WebSecurityConfig.class,
    LocaleConfiguration.class,
    MappingConfiguration.class

})
@ComponentScan("com.lccf")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
