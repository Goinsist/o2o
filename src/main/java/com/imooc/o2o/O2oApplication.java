package com.imooc.o2o;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class O2oApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(O2oApplication.class, args);
    }


    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application){
        return application.sources(O2oApplication.class);
    }
}
