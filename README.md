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

2\. Hotel模块

---

### Comment模块

**1.1\. Comment CRUD**
###### 接口功能
> 评论的增加、删除、修改、查询功能

###### 使用方式
> 增加(POST [http://localhost:9002/api/comments])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|writerId |true    |long|发表评论的用户id|
|hotelId |true    |long|评论对应的酒店id|
|ranking |true    |int|评论星级|
|content |true    |String|评论内容|
|dateCreated |false    |String|发布日期时间YYYY-MM-DD HH:mm:ss|

> 修改(PUT [http://localhost:9002/api/comments/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|writerId |false    |long|发表评论的用户id|
|hotelId |false    |long|评论对应的酒店id|
|ranking |false    |int|评论星级|
|content |false    |String|评论内容|
|dateCreated |false    |String|发布日期时间YYYY-MM-DD HH:mm:ss|

> 删除(DELETE [http://localhost:9002/api/comments/${id}] [管理员])

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
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                ...
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
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
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                ...
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
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
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":1,
                ...
            }
        },
        {
            "id":2,
            "dateCreated":"2018-12-18T12:48:25.375",
            "writer":{
                "id":108,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
            }
        },
        {
            "id":3,
            "dateCreated":"2018-12-18T12:48:25.378",
            "writer":{
                "id":109,
                ...
            },
            "ranking":3,
            "content":"我喔喔喔喔",
            "hotel":{
                "id":2,
                ...
            }
        }
    ],
    "commentCount":3
}
```
### Hotel模块

**1.1\. Hotel CRUD**
###### 接口功能
> 酒店/民宿的增加、删除、修改、查询功能

###### 使用方式
> 查询(GET [http://localhost:9002/api/hotels])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|name |false    |String|模糊匹配的名字|
|minGrand |false    |int|最小星级 默认：1|
|maxGrand |false    |int|最大星级 默认：7|
|hotelType |false    |String|酒店（HOTEL）/民宿（HOMESTAY）|
|position |false    |String|查询中心点坐标|
|distance |false    |int|查询范围距中心点半径 单位：公里|

> 增加(POST [http://localhost:9002/api/hotels])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|name |true    |String|酒店/民宿名称|
|totalRanking |true    |int|总评分数|
|commenterCount |true    |int|总评论数|
|location |true    |String|具体位置描述|
|description |true    |String|酒店描述|
|photos |false    |String[]|酒店照片url集合|
|hotelType |true    |String|酒店类型：酒店（HOTEL）/民宿（HOMESTAY）|
|tags |false    |String[]|酒店标签|
|managerId |true    |Long|酒店管理员id|
|dateCreated |true    |String|发布日期时间YYYY-MM-DD HH:mm:ss|
|englishName |true    |String|酒店/民宿英文名称|
|grand |true    |int|酒店星级|
|facilities |false    |String[]|酒店设施|
|contact |true    |String|酒店联系方式|

> 修改(PUT [http://localhost:9002/api/hotels/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|name |true    |String|酒店/民宿名称|
|totalRanking |true    |int|总评分数|
|commenterCount |true    |int|总评论数|
|location |true    |String|具体位置描述|
|description |true    |String|酒店描述|
|photos |false    |String[]|酒店照片url集合|
|hotelType |true    |String|酒店类型：酒店（HOTEL）/民宿（HOMESTAY）|
|tags |false    |String[]|酒店标签|
|managerId |true    |Long|酒店管理员id|
|dateCreated |true    |String|发布日期时间YYYY-MM-DD HH:mm:ss|
|englishName |true    |String|酒店/民宿英文名称|
|grand |true    |int|酒店星级|
|facilities |false    |String[]|酒店设施|
|contact |true    |String|酒店联系方式|

> 删除(DELETE [http://localhost:9002/api/hotels/${id}] [管理员])

