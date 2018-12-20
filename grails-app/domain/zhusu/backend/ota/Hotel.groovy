package zhusu.backend.ota

import net.kaleidos.hibernate.usertype.ArrayType
import zhusu.backend.user.User

import com.vividsolutions.jts.geom.Point
import java.time.LocalDateTime

class Hotel {

    String name
    long totalRanking
    long commenterCount
    String location
    Point point
    String description
    String[] photos
    String hotelType
    String[] tags
    User manager
    LocalDateTime dateCreated
    String englishName
    int grand
    String[] facilities
    String contact

    static constraints = {
        name nullable: false, blank: false, maxSize: 50
        location nullable: false, blank: false, maxSize: 50
        point nullable: false
        description nullable: false, blank: false, maxSize: 500
        photos nullable: true
        hotelType nullable: false, inList: availableTypes()
        tags nullable: true
        manager nullable: false
        englishName nullable: false, blank: false, maxSize: 100
        grand min: 1, max: 7
        facilities nullable: true
        contact nullable: false, blank: false, maxSize: 50
    }

    static mapping = {
        comment '酒店信息'
        name comment: '名称'
        totalRanking comment: '总评分'
        commenterCount comment: '评价人数'
        location comment: '位置描述'
        point comment: '地理位置信息'
        description comment: '描述'
        photos comment: '照片URL', type: ArrayType, params: [type: String]
        hotelType comment: '酒店类型'
        tags comment: '分类标签', type: ArrayType, params: [type: String]
        manager comment: '管理员'
        dateCreated comment: '创建时间'
        englishName comment: '英文名称'
        grand comment: '酒店星级'
        facilities comment: '酒店设施', type: ArrayType, params: [type: String]
        contact comment: '酒店联系方式'
    }

    static List<String> availableTypes() {
        [
                'HOTEL'        // 酒店
                , 'HOMESTAY'   // 民宿
        ]
    }

}
