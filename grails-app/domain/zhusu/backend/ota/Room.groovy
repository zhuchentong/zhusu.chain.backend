package zhusu.backend.ota

import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.usertype.JsonbMapType

import java.time.LocalDateTime

class Room {

    Hotel hotel
    String roomNumber
    String[] photos
    Map properties
    long price
    boolean opened = false
    LocalDateTime dateCreated

    static constraints = {
        hotel nullable: false
        roomNumber nullable: false, blank: false, maxSize: 10, unique: 'hotel'
        photos nullable: true
        properties nullable: true
    }

    static mapping = {
        comment '房间'
        hotel comment: '所属酒店'
        roomNumber comment: '房间号'
        photos comment: '房间照片', type: ArrayType, params: [type: String]
        properties comment: '属性', type: JsonbMapType
        price comment: '价格'
        opened comment: '是否开放'
        dateCreated comment: '创建时间'
    }

}
