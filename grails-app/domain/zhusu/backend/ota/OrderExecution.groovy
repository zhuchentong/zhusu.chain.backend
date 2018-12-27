package zhusu.backend.ota

import zhusu.backend.user.User

import java.time.LocalDateTime

class OrderExecution {

    Order order
    String status
    String memo
    User operator
    LocalDateTime dateCreated

    static constraints = {
        order nullable: false
        status nullable: false, maxSize: 20, inList: availableStatus()
        memo nullable: true, maxSize: 200
        operator nullable: false
    }

    static mapping = {
        comment '酒店订单执行跟踪'
        version false
        order comment: '订单'
        status comment: '状态'
        memo comment: '备注'
        operator comment: '更改人'
        dateCreated comment: '创建时间'
    }

    static List<String> availableStatus() {
        [
                'CREATED',      //创建
                'CONFIRMED'     // 确认
                , 'CHECKIN'       // 入住
                , 'CHECKOUT'      // 离开
                , 'CANCELED'      // 取消
        ]
    }

}
