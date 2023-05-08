package com.pract.crud;

import com.pract.crud.entity.User;
import com.pract.crud.entity.UserSetting;
import com.pract.crud.repository.UserRepository;
import com.pract.crud.repository.UserSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CrudApplication.class, args);
        Object dataSource = context.getBean("dataSource");
        System.out.println(dataSource);
    }

    @Bean
    CommandLineRunner seedDB(UserRepository userRepository, UserSettingRepository userSettingRepository) {
        return args -> {
            User saved = userRepository.save(new User(0,
                    "0000000000123456",
                    "fisrt",
                    "middle",
                    "family",
                    new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
                    true,
                    null));
            Stream.of(new String[][]{
                            {"biometric_login", "false"},
                            {"push_notification", "false"},
                            {"sms_notification", "false"},
                            {"show_onboarding", "false"},
                            {"widget_order", "1,2,3,4,5"},
                    }).map(data -> new UserSetting(0, data[0], data[1], saved))
                    .collect(Collectors.toList())
                    .forEach(setting -> userSettingRepository.save(setting)
                    );
        };
    }
}
