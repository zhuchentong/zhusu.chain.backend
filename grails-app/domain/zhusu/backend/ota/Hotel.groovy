package zhusu.backend.ota

import net.kaleidos.hibernate.usertype.ArrayType
import zhusu.backend.user.User

import java.time.LocalDateTime

class Hotel {

    String name
    long totalRanking
    long commenterCount
    String location
    String description
    String[] photos
    String hotelType
    String[] tags
    User manager
    LocalDateTime dateCreated

    static constraints = {
        name nullable: false, blank: false, maxSize: 50
        location nullable: false, blank: false, maxSize: 50
        description nullable: false, blank: false, maxSize: 500
        photos nullable: true
        hotelType nullable: false, inList: availableTypes()
        tags nullable: true
        manager nullable: false
    }

    static mapping = {
        comment '酒店信息'
        name comment: '名称'
        totalRanking comment: '总评分'
        commenterCount comment: '评价人数'
        location comment: '位置'
        description comment: '描述'
        photos comment: '照片URL', type: ArrayType, params: [type: String]
        hotelType comment: '酒店类型'
        tags comment: '分类标签', type: ArrayType, params: [type: String]
        manager comment: '管理员'
        dateCreated comment: '创建时间'
    }

    static List<String> availableTypes() {
        [
                'HOTEL'        // 酒店
                , 'HOMESTAY'   // 民宿
        ]
    }

}
