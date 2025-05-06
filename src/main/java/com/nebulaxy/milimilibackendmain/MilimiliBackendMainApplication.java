package com.nebulaxy.milimilibackendmain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nebulaxy.milimilibackendmain.mapper")
public class MilimiliBackendMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilimiliBackendMainApplication.class, args);
    }

}
