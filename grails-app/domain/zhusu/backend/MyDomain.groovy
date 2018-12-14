package zhusu.backend

import com.vividsolutions.jts.geom.Point
import net.kaleidos.hibernate.usertype.ArrayType
import net.kaleidos.hibernate.usertype.JsonbMapType

import java.time.LocalDateTime

class MyDomain {

    Map kvPair
    String[] strings
    Point location
    LocalDateTime dateCreated

    static mapping = {
        kvPair comment: 'Jsonb示例', type: JsonbMapType
        strings comment: '数组示例', type: ArrayType, params: [type: String]
        location comment: '位置信息'
    }
}
