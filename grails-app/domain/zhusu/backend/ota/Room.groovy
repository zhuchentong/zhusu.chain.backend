package zhusu.backend.ota

import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.usertype.JsonbMapType

import java.time.LocalDateTime

class Room {

    String name
    Hotel hotel
    String[] photos
    Map attributes
    long price
    int total
    LocalDateTime dateCreated

    static constraints = {
        name nullable: false, blank: false, maxSize: 50
        hotel nullable: false
        photos nullable: true
        total nullable: false, min: 0, max: 100
        attributes nullable: true
    }

    static mapping = {
        comment '房间'
        name comment: '房间名称'
        hotel comment: '所属酒店'
        photos comment: '房间照片', type: ArrayType, params: [type: String]
        attributes comment: '属性', type: JsonbMapType
        price comment: '价格'
        total comment: '总量'
        dateCreated comment: '创建时间'
    }

}
