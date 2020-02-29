### 基于springboot和thymeleaf开发的一套博客模板
#### 1.使用前请前往src/main/resources/application-pro.yml中设置数据库，默认为localhost
#### 2.使用前请前往src/main/resources/i18n/messages.properties自定义参数
#### 3.后台默认访问地址为localhost/admin，账号admin密码123456
#### 4.管理员密码默认为md5加密，设置密码前需要将密码md5加密后存入数据库
#### 5.如果不喜欢网站图标，可以更换src/main/resources/static/favicon.ico文件为自己喜欢的图片，名称为favicon.ico即可
#### 6.使用sql之前请新建blog数据库，否则会建表失败
#### 建议打成jar包，使用docker部署