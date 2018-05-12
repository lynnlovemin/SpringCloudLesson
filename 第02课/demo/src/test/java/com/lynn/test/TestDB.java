package com.lynn.test;

import com.lynn.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = HelloController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDB {

    @Test
    public void test(){
    }
}
