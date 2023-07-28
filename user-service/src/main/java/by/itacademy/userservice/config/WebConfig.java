package by.itacademy.userservice.config;

import by.itacademy.userservice.core.formatters.LocalDateTimeToMilliFormatter;
import by.itacademy.userservice.core.converters.UserEntityToUserDTOConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new UserEntityToUserDTOConverter());
        registry.addFormatter(new LocalDateTimeToMilliFormatter());
    }
}
