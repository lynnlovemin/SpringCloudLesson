package com.lynn.demo;

import com.lynn.demo.zk.DistributedLock;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("index")
    public String index()throws Exception{
        DistributedLock lock   = new DistributedLock("localhost:2181","lock");
        lock.lock();
        //共享资源
        if(lock != null){
            System.out.println("执行方法");
            Thread.sleep(5000);
            lock.unlock();
        }
        return "hello world!";
    }
}
