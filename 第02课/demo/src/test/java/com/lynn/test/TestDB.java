package com.lynn.test;

import com.lynn.AliyunAuto;
import com.lynn.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDB {

    @Autowired
    private AliyunAuto aliyun;

    @Test
    public void test(){
        System.out.println(aliyun);
    }
}
