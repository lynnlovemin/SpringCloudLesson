package com.lynn;

import com.lynn.version.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("{version}")
@ApiVersion(1)
public class HelloController {

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

    @ExceptionHandler
    public String doError(Exception ex) throws Exception{
        ex.printStackTrace();
        return ex.getMessage();
    }

}