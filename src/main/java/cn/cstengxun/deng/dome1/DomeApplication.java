package cn.cstengxun.deng.dome1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName DomeApplication
 * @Description TODO
 * @Author Mr.Deng
 * @Date 2023/11/29 15:50
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("cn.cstengxun.deng.dome1")
@ComponentScan("cn.cstengxun.deng")
public class DomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DomeApplication.class,args);
    }
}
