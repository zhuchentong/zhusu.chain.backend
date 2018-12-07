# 住宿链后端API

## 配置事项

1. 由于身份证采用了DES加密，因此项目中需要相应的key，其位于 application.yml 中的 des_key ，配置如下：

~~~
des_key: ${DES_KEY:secret12}
~~~

其中 secret12 为默认key，用于开发环境。在产品环境下，请配置环境变量 DES_KEY 以替换此内容。同时，也需注意，对于密钥改变对于数据库内相应数据的影响。