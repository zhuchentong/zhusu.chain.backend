# 住宿链后端API

## 配置事项

1. 由于身份证采用了DES加密，因此项目中需要相应的key，其位于 application.yml 中的 des_key ，配置如下：

~~~
des_key: ${DES_KEY:secret12}
~~~

其中 secret12 为默认key，用于开发环境。在产品环境下，请配置环境变量 DES_KEY 以替换此内容。同时，也需注意，对于密钥改变对于数据库内相应数据的影响。

# 住宿链后端API手册 v1.0
目录

1\. Comment模块

---

### Comment模块

**1.1\. Comment CRUD**
###### 接口功能
> 评论的增加、删除、修改、查询功能

###### 使用方式
> 参照Restful 标准化调用方式

**1.2\. listByHotel**
###### 接口功能
> 根据酒店（民宿）id查询该酒店（民宿）的所有评论

###### URL
> [http://localhost:9002/api/comments/listByHotel](http://localhost:9002/api/comments/listByHotel)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|hotelId |true    |long|请求的酒店（民宿）id|

###### 返回字段
>
|返回字段|字段类型|说明                              |
|:-----   |:------|:-----------------------------   |
|id   |long    |此条评论的id。      |
|dateCreated  |date | 评论创建时间                        |
|writer |User |此条评论的作者                          |
|ranking |int |此条评论的评级                         |
|content |String |此条评论的内容                         |
|hotel |Hotel |此条评论对应的的酒店（民宿）                         |

###### 接口示例
> 地址：[http://localhost:9002/api/comments/listByHotel?hotelId=1](http://localhost:9002/api/comments/listByHotel?hotelId=1)
``` javascript
{
    "commentList":[
        {
            "id":1,
            "dateCreated":"2018-12-18T12:48:25.37",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.357",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":108,
                    "dateCreated":"2018-12-18T12:48:25.331",
                    "passwordExpired":false,
                    "username":"1350000001",
                    "enabled":true,
                    "displayName":"1350000001",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                "dateCreated":"2018-12-18T12:48:25.338",
                "passwordExpired":false,
                "username":"1350000002",
                "enabled":true,
                "displayName":"1350000002",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        }
    ],
    "commentCount":3
}
```
**1.3\. listByUser**
###### 接口功能
> 根据用户id查询该用户的所有评论

###### URL
> [http://localhost:9002/api/comments/listByUser](http://localhost:9002/api/comments/listByUser)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|userId |true    |long|请求的用户id|

###### 返回字段
>
|返回字段|字段类型|说明                              |
|:-----   |:------|:-----------------------------   |
|id   |long    |此条评论的id。      |
|dateCreated  |date | 评论创建时间                        |
|writer |User |此条评论的作者                          |
|ranking |int |此条评论的评级                         |
|content |String |此条评论的内容                         |
|hotel |Hotel |此条评论对应的的酒店（民宿）                         |

###### 接口示例
> 地址：[http://localhost:9002/api/comments/listByUser?userId=1](http://localhost:9002/api/comments/listByHotel?userId=1)
``` javascript
{
    "commentList":[
        {
            "id":1,
            "dateCreated":"2018-12-18T12:48:25.37",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.357",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":108,
                    "dateCreated":"2018-12-18T12:48:25.331",
                    "passwordExpired":false,
                    "username":"1350000001",
                    "enabled":true,
                    "displayName":"1350000001",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                "dateCreated":"2018-12-18T12:48:25.338",
                "passwordExpired":false,
                "username":"1350000002",
                "enabled":true,
                "displayName":"1350000002",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        }
    ],
    "commentCount":3
}
```
**1.4\. listByRanking**
###### 接口功能
> 查询对应星级的所有评论

###### URL
> [http://localhost:9002/api/comments/listByRanking](http://localhost:9002/api/comments/listByRanking)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|ranking |true    |int(1-5)|请求的星级|

###### 返回字段
>
|返回字段|字段类型|说明                              |
|:-----   |:------|:-----------------------------   |
|id   |long    |此条评论的id。      |
|dateCreated  |date | 评论创建时间                        |
|writer |User |此条评论的作者                          |
|ranking |int |此条评论的评级                         |
|content |String |此条评论的内容                         |
|hotel |Hotel |此条评论对应的的酒店（民宿）                         |

###### 接口示例
> 地址：[http://localhost:9002/api/comments/listByRanking?ranking=3](http://localhost:9002/api/comments/listByRanking?ranking=3)
``` javascript
{
    "commentList":[
        {
            "id":1,
            "dateCreated":"2018-12-18T12:48:25.37",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.357",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":108,
                    "dateCreated":"2018-12-18T12:48:25.331",
                    "passwordExpired":false,
                    "username":"1350000001",
                    "enabled":true,
                    "displayName":"1350000001",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                "dateCreated":"2018-12-18T12:48:25.331",
                "passwordExpired":false,
                "username":"1350000001",
                "enabled":true,
                "displayName":"1350000001",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                "dateCreated":"2018-12-18T12:48:25.338",
                "passwordExpired":false,
                "username":"1350000002",
                "enabled":true,
                "displayName":"1350000002",
                "role":[
                    "ROLE_YH"
                ]
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                "hotelType":"HOTEL",
                "dateCreated":"2018-12-18T12:48:25.36",
                "location":"北京市天安门广场",
                "grand":4,
                "contact":"110",
                "commenterCount":49,
                "manager":{
                    "id":109,
                    "dateCreated":"2018-12-18T12:48:25.338",
                    "passwordExpired":false,
                    "username":"1350000002",
                    "enabled":true,
                    "displayName":"1350000002",
                    "role":[
                        "ROLE_YH"
                    ]
                },
                "name":"北京和颐酒店222",
                "englishName":"BeiJingHeYi",
                "totalRanking":123,
                "description":"4星级酒店"
            }
        }
    ],
    "commentCount":3
}
```