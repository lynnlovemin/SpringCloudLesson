package com.lynn.demo.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedLock implements Lock, Watcher{
    private ZooKeeper zk;
    private String root = "/locks";//根
    private String lockName;//竞争资源的标志
    private String waitNode;//等待前一个锁
    private String myZnode;//当前锁
    private CountDownLatch latch;//计数器
    private CountDownLatch connectedSignal=new CountDownLatch(1);
    private int sessionTimeout = 30000;
    /**
     * 创建分布式锁,使用前请确认config配置的zookeeper服务可用
     * @param config localhost:2181
     * @param lockName 竞争资源标志,lockName中不能包含单词_lock_
     */
    public DistributedLock(String config, String lockName){
        this.lockName = lockName;
        // 创建一个与服务器的连接
        try {
            zk = new ZooKeeper(config, sessionTimeout, this);
            connectedSignal.await();
            Stat stat = zk.exists(root, false);//此去不执行 Watcher
            if(stat == null){
                // 创建根节点
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            throw new LockException(e);
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }
    /**
     * zookeeper节点的监视器
     */
    public void process(WatchedEvent event) {
        //建立连接用
        if(event.getState()== Event.KeeperState.SyncConnected){
            connectedSignal.countDown();
            return;
        }
        //其他线程放弃锁的标志
        if(this.latch != null) {
            this.latch.countDown();
        }
    }

    public void lock() {
        try {
            if(this.tryLock()){
                System.out.println("Thread " + Thread.currentThread().getId() + " " +myZnode + " get lock true");
                return;
            }
            else{
                waitForLock(waitNode, sessionTimeout);//等待锁
            }
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }
    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if(lockName.contains(splitStr))
                throw new LockException("lockName can not contains \\u000B");
            //创建临时子节点
            myZnode = zk.create(root + "/" + lockName + splitStr, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(myZnode + " is created ");
            //取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);
            //取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<String>();
            for (String node : subNodes) {
                String _node = node.split(splitStr)[0];
                if(_node.equals(lockName)){
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);

            if(myZnode.equals(root+"/"+lockObjNodes.get(0))){
                //如果是最小的节点,则表示取得锁
                System.out.println(myZnode + "==" + lockObjNodes.get(0));
                return true;
            }
            //如果不是最小的节点，找到比自己小1的节点
            String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
            waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);//找到前一个子节点
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
        return false;
    }
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            if(this.tryLock()){
                return true;
            }
            return waitForLock(waitNode,time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean waitForLock(String lower, long waitTime) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(root + "/" + lower,true);//同时注册监听。
        //判断比自己小一个数的节点是否存在,如果不存在则无需等待锁,同时注册监听
        if(stat != null){
            System.out.println("Thread " + Thread.currentThread().getId() + " waiting for " + root + "/" + lower);
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);//等待，这里应该一直等待其他线程释放锁
            this.latch = null;
        }
        return true;
    }
    public void unlock() {
        try {
            System.out.println("unlock " + myZnode);
            zk.delete(myZnode,-1);
            myZnode = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }
    public Condition newCondition() {
        return null;
    }

    public class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public LockException(String e){
            super(e);
        }
        public LockException(Exception e){
            super(e);
        }
    }
}