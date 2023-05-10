package com.pract.crud;

import com.pract.crud.entity.User;
import com.pract.crud.entity.UserSetting;
import com.pract.crud.repository.UserRepository;
import com.pract.crud.repository.UserSettingRepository;
import com.pract.crud.util.Constant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CrudApplication.class, args);
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
                    true));
            Constant.INITIAL_SETTINGS.entrySet().stream()
                    .map(data -> new UserSetting(0, data.getKey(), data.getValue(), saved))
                    .toList()
                    .forEach(userSettingRepository::save);
        };
    }
}
