package zhusu.backend.ota

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import spock.lang.Unroll
import zhusu.backend.user.User
import zhusu.backend.utils.TestUtils

@Integration
@Rollback
class RoomFunctionalSpec extends Specification{

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#role 可以看到房间列表并查看详情"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Hotel hotel
        Room room
        Room.withNewTransaction {
            User currentUser = TestUtils.createUser(role, '13500000001')
            User manager = TestUtils.createUser('ROLE_YH', '1350000002')
            hotel = new Hotel(name: '王府井酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'WangFuJingBeiJing', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(100, 55))).save()
            Hotel testHotel = new Hotel(name: '王府井酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'WangFuJingBeiJing', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(100, 55))).save()
            room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            new Room(name: '标准双人间', hotel: hotel, price: 23456, total: 10, dataCreated: '2018-11-11 12:12:12').save()
            new Room(name: '标准双人间', hotel: testHotel, price: 23456, total: 10, dataCreated: '2018-11-11 12:12:12').save()
        }

        when:
        String jwt
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/rooms?hotelId=${hotel.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.roomCount == count

        when:
        response = rest.get("http://localhost:${serverPort}/api/rooms/${room.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == 200

        where:
        role          | count
        'ROLE_ADMIN'  | 2
        'ROLE_SELLER' | 2
        'ROLE_YH'     | 2
        null          | 2
    }

    @Unroll
    void "#role #should 创建、编辑、删除房间"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        Hotel myHotel
        Room room
        Room.withNewTransaction {
            currentUser = TestUtils.createUser(role, '13500000001')
            myHotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            room = new Room(name: '标准大床房', hotel: myHotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/rooms") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = '标准双人间'
                hotel {
                    id = myHotel.id
                }
                price = 12345
                total = 20
            }
        }

        then:
        response.status == postStatus
        if (201 == postStatus) {
            assert Room.count == 2
        }

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/rooms/${room.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = 'updated'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            room.refresh()
            assert room.name == 'updated'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/rooms/${room.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Room.count() == 1
        }

        where:
        role          | should    | postStatus | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 201        | 200       | 204
        'ROLE_SELLER' | 'can not' | 403        | 403       | 403
        'ROLE_YH'     | 'can not' | 403        | 403       | 403
        null          | 'can not' | 401        | 401       | 401
    }

    static void main(String[] args) {
        RestBuilder rest = new RestBuilder()
        RestResponse response = rest.post("http://localhost:9002/api/rooms") {
            json {
                name = '标准双人间'
                hotel {
                    id = 1
                }
                price = 12345
                total = 20
            }
        }
        println(response.status)
    }

}
