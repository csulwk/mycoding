# mycoding-server

## Description 
> **front-back stage decoupling framework**  
> **GitHub: [https://github.com/csulwk/mycoding.git](https://github.com/csulwk/mycoding.git)**  

## Environment
* `SpringBoot 2.2.1.RELEASE`  

## Framework
```
mycoding-server  
├─ src  
│  ├─ main
│  │  ├─ java
│  │  │  ├─ com.mc.coding
│  │  │  │  ├─ controller                   -- 请求类
│  │  │  │  ├─ dao                          -- 查询类
│  │  │  │  ├─ entity                       -- 实体类
│  │  │  │  └─ service                      -- 业务类
│  │  │  └─ MycodingServerApplication.java  -- 启动类
│  │  └─ resources
│  │     ├─ mapper                          -- 映射配置
│  │     ├─ bootstrap.yml                   -- 启动配置
│  │     └─ application.yml                 -- 应用配置
│  └─ test                                  -- 测试类
├─ pom.xml
├─ .gitignore
└─ README.md
```

