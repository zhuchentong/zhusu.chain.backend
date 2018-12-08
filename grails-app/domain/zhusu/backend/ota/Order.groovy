package zhusu.backend.ota

import zhusu.backend.user.User

import java.time.LocalDateTime

class Order {

    User buyer
    Room room
    LocalDateTime beginDate
    LocalDateTime endDate
    User friend1
    User friend2
    User friend3
    User friend4
    String memo
    String status = 'CREATED'

    static constraints = {
        buyer nullable: false
        room nullable: false
        beginDate nullable: false
        endDate nullable: false
        friend1 nullable: true
        friend2 nullable: true
        friend3 nullable: true
        friend4 nullable: true
        memo nullable: true, maxSize: 100
        status nullable: false, maxSize: 20, inList: availableStatus()
    }

    static mapping = {
        comment '酒店订单'
        buyer comment: '下单人'
        room comment: '房间'
        beginDate comment: '入住时间'
        endDate comment: '离开时间'
        friend1 comment: '同行人1'
        friend2 comment: '同行人2'
        friend3 comment: '同行人3'
        friend4 comment: '同行人4'
        memo comment: '备注'
        status comment: '状态'
    }

    static List<String> availableStatus() {
        [
                'CREATED'         // 创建
                , 'CONFIRMED'     // 确认
                , 'CHECKIN'       // 入住
                , 'CHECKOUT'      // 离开
                , 'CANCELED'      // 取消
        ]
    }

}
