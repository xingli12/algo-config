package com.company.algo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAspectJAutoProxy;

/**
 * 算法配置管理系统主应用程序
 *
 * @author Algo Config Team
 * @since 1.0.0
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.company.algo.repository")
public class AlgoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgoConfigApplication.class, args);
    }
}
