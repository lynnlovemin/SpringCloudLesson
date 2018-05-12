package com.lynn;

public class Aliyun {

    private String appKey;

    private String appSecret;

    private String bucket;

    private String endPoint;

    public static class Builder{

        private String appKey;

        private String appSecret;

        private String bucket;

        private String endPoint;

        public Builder setAppKey(String appKey){
            this.appKey = appKey;
            return this;
        }

        public Builder setAppSecret(String appSecret){
            this.appSecret = appSecret;
            return this;
        }

        public Builder setBucket(String bucket){
            this.bucket = bucket;
            return this;
        }

        public Builder setEndPoint(String endPoint){
            this.endPoint = endPoint;
            return this;
        }

        public Aliyun build(){
            return new Aliyun(this);
        }
    }

    public static Builder options(){
        return new Aliyun.Builder();
    }

    private Aliyun(Builder builder){
        this.appKey = builder.appKey;
        this.appSecret = builder.appSecret;
        this.bucket = builder.bucket;
        this.endPoint = builder.endPoint;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getBucket() {
        return bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        return "Aliyun{" +
                "appKey='" + appKey + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", bucket='" + bucket + '\'' +
                ", endPoint='" + endPoint + '\'' +
                '}';
    }
}
