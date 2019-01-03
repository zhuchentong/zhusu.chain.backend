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

3\. Room模块

4\. Post模块

5\. Order模块

6\. OrderExecution模块

---

### Comment模块

**1.1\. Comment CRUD**
###### 接口功能
> 评论的增加、删除、修改、查询功能

###### 使用方式
> 增加(POST [http://localhost:9002/api/comments])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|
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
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|
|name |false    |String|模糊匹配的名字|
|minGrand |false    |int|最小星级 默认：1|
|maxGrand |false    |int|最大星级 默认：7|
|hotelType |false    |String|酒店（HOTEL）/民宿（HOMESTAY）|
|position |false    |String|查询中心点坐标|
|distance |false    |int|查询范围距中心点半径 单位：公里|

> 增加(POST [http://localhost:9002/api/hotels] [管理员])

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

### Room模块

**1.1\. Room CRUD**
###### 接口功能
> 房间的增加、删除、修改、查询功能

###### 使用方式
> 查询(GET [http://localhost:9002/api/rooms])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|
|hotelId |false    |String|酒店（HOTEL）/民宿（HOMESTAY）|

> 增加(POST [http://localhost:9002/api/rooms] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|name |true    |String|房间名称|
|hotel |true    |Hotel|所属酒店（仅包含id字段）|
|photos |false    |String[]|照片列表|
|total |true    |String|此类型房间数量|
|attributes |false    |Map<String, Object>|房间描述|

> 修改(PUT [http://localhost:9002/api/rooms/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|name |false    |String|房间名称|
|hotel |false    |Hotel|所属酒店（仅包含id字段）|
|photos |false    |String[]|照片列表|
|total |false    |String|此类型房间数量|
|attributes |false    |Map<String, Object>|房间描述|

> 删除(DELETE [http://localhost:9002/api/rooms/${id}] [管理员])

### Post模块

**1.1\. Post CRUD**
###### 接口功能
> 公告的增加、删除、修改、查询功能

###### 使用方式
> 查询(GET [http://localhost:9002/api/posts])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|
|published |false    |String|已发布（published）/未发布（unpublished）/全部(undefined)|

> 增加(POST [http://localhost:9002/api/posts] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|title |true    |String|房间名称|
|content |true    |Hotel|所属酒店（仅包含id字段）|

> 修改(PUT [http://localhost:9002/api/posts/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|title |false    |String|房间名称|
|content |false    |Hotel|所属酒店（仅包含id字段）|

> 删除(DELETE [http://localhost:9002/api/posts/${id}] [管理员])

### Order模块

**1.1\. Order CRUD**
###### 接口功能
> 订单的增加、删除、修改、查询功能

###### 使用方式
> 查询(GET [http://localhost:9002/api/orders])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|

> 增加(POST [http://localhost:9002/api/orders] [用户、管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|user |true    |User|购买用户|
|room |true    |Room|预定酒店|
|beginDate |true    |String|入住日期 ex：2018-09-09 00：00：00|
|endDate |true    |String| 退房日期 ex：2018-09-10 00：00：00|
|attributes |false    |Map<String, Object>|订单其他属性|
|memo |false    |String|备注|

> 修改(PUT [http://localhost:9002/api/orders/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|user |true    |User|购买用户|
|room |true    |Room|预定酒店|
|beginDate |true    |String|入住日期 ex：2018-09-09 00：00：00|
|endDate |true    |String| 退房日期 ex：2018-09-10 00：00：00|
|attributes |false    |Map<String, Object>|订单其他属性|
|memo |false    |String|备注|

> 删除(DELETE [http://localhost:9002/api/orders/${id}] [管理员])

**1.2\. confirm**
###### 接口功能
> 将订单由·创建·状态变更为·已确认·状态

###### URL
> [http://localhost:9002/api/orders/confirm](http://localhost:9002/api/orders/confirm [管理员、商家])

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|id |true    |long|需要变更状态的订单id|

###### 接口示例
> 地址：[http://localhost:9002/api/orders/confirm?id=1](http://localhost:9002/api/orders/confirm?id=1)

**1.3\. checkIn**
###### 接口功能
> 将订单由·已确认·状态变更为·已入住·状态

###### URL
> [http://localhost:9002/api/orders/checkIn](http://localhost:9002/api/orders/checkIn [管理员、商家])

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|id |true    |long|需要变更状态的订单id|

###### 接口示例
> 地址：[http://localhost:9002/api/orders/checkIn?id=1](http://localhost:9002/api/orders/checkIn?id=1)

**1.4\. checkOut**
###### 接口功能
> 将订单由·已入住·状态变更为·已离店·状态

###### URL
> [http://localhost:9002/api/orders/checkOut](http://localhost:9002/api/orders/checkOut [管理员、商家])

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|id |true    |long|需要变更状态的订单id|

###### 接口示例
> 地址：[http://localhost:9002/api/orders/checkOut?id=1](http://localhost:9002/api/orders/checkOut?id=1)

**1.5\. cancel**
###### 接口功能
> 将订单由·非取消·状态变更为·取消·状态

###### URL
> [http://localhost:9002/api/orders/cancel](http://localhost:9002/api/orders/cancel [管理员])

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|id |true    |long|需要变更状态的订单id|

###### 接口示例
> 地址：[http://localhost:9002/api/orders/cancel?id=1](http://localhost:9002/api/orders/cancel?id=1)

### OrderExecution模块

**1.1\. OrderExecution CRUD**
###### 接口功能
> 订单执行步骤的增加、删除、修改、查询功能

###### 使用方式
> 查询(GET [http://localhost:9002/api/orderExecutions])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|max |false    |int|分页最大条数|
|sort |false    |String|排序字段（默认：'id'）|
|order |false    |String|正序（ESC）/倒序（DESC）|

> 增加(POST [http://localhost:9002/api/orderExecutions] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|order |true    |Order|所属订单|
|status |true    |String|当前步骤状态|
|memo |false    |String|备注|

> 修改(PUT [http://localhost:9002/api/orderExecutions/${id}] [管理员])

|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|order |true    |Order|所属订单|
|status |true    |String|当前步骤状态|
|memo |false    |String|备注|

> 删除(DELETE [http://localhost:9002/api/orderExecutions/${id}] [管理员])

**1.2\. last**
###### 接口功能
> 将订单由·非取消·状态变更为·取消·状态

###### URL
> [http://localhost:9002/api/orderExecutions/last](http://localhost:9002/api/orderExecutions/last [管理员])

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数
>
|参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|orderId |true    |long|需要获取最新执行步骤的订单id|

###### 接口示例
> 地址：[http://localhost:9002/api/orderExecutions/last?orderId=1](http://localhost:9002/api/orderExecutions/last?orderId=1)
