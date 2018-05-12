package com.lynn;

import com.lynn.version.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("{version}")
@ApiVersion(1)
public class HelloController extends SpringBootServletInitializer{

    @Value("${server.port}")
    String port;
    @Autowired
    private Aliyun aliyun;
    @RequestMapping("/hello")
    public String home(String name) {
        return "hi "+name+",i am from port:" +port+aliyun;
    }

    @RequestMapping("authorize")
    public void authorize(@Valid AuthorizeIn authorize, BindingResult result){
        if(result.hasFieldErrors()){
            List<FieldError> errorList = result.getFieldErrors();
            //通过断言抛出参数不合法的异常
            errorList.stream().forEach(item -> Assert.isTrue(false,item.getDefaultMessage()));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloController.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HelloController.class);
    }
}