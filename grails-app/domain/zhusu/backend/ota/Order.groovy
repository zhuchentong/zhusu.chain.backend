package zhusu.backend.ota

import net.kaleidos.hibernate.usertype.JsonbMapType
import zhusu.backend.user.User

import java.time.LocalDateTime

class Order {

    User buyer
    Room room
    LocalDateTime beginDate
    LocalDateTime endDate
    Map attributes
    String memo
    String status = 'CREATED'

    static constraints = {
        buyer nullable: false
        room nullable: false
        beginDate nullable: false
        endDate nullable: false
        memo nullable: true, maxSize: 100
        attributes nullable: true
        status nullable: false, maxSize: 20, inList: availableStatus()
    }

    static mapping = {
        comment '酒店订单'
        buyer comment: '下单人'
        room comment: '房间'
        beginDate comment: '入住时间'
        endDate comment: '离开时间'
        memo comment: '备注'
        attributes comment: '属性', type: JsonbMapType
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
