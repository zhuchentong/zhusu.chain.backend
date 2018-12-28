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
class HotelFunctionalSpec extends Specification{

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    void "#role 可以看到酒店/民宿列表并查看详情"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Hotel hotel
        Hotel.withNewTransaction {
            User currentUser = TestUtils.createUser(role, '13500000001')
            User manager = TestUtils.createUser('ROLE_YH', '1350000002')
            new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            hotel = new Hotel(name: '王府井酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'WangFuJingBeiJing', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(100, 55))).save()
            new Hotel(name: '北京和颐酒店3', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: manager, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            new Hotel(name: '北京和颐酒店4', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: manager, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            new Hotel(name: '北京和颐酒店5', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: manager, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            new Hotel(name: '北京和颐酒店6', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: manager, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
        }

        when:
        String jwt
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/hotels?name=王府井&minGrand=4&maxGrand=4&hotelType=HOTEL&lat=100&lng=55") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.hotelCount == count

        when:
        response = rest.get("http://localhost:${serverPort}/api/hotels/${hotel.id}"){
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == 200

        where:
        role          | count
        'ROLE_ADMIN'  | 1
        'ROLE_SELLER' | 1
        'ROLE_YH'     | 1
        null          | 1
    }

    @Unroll
    void "#role #should 创建、编辑、删除酒店/民宿"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        Hotel hotel
        Hotel.withNewTransaction {
            currentUser = TestUtils.createUser(role, '13500000001')
            hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: currentUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/hotels") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = '北京和颐酒店2'
                location = '北京市天安门广场'
                description = '4星'
                hotelType = 'HOTEL'
                manager {
                    id = currentUser.id
                }
                dateCreated = '2018-09-09 12:12:12'
                englishName = 'BeiJingHeYi'
                grand = 4
                contact = '110'
                position {
                    lat = 101.1010
                    lng = 37.9887
                }
            }
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert Hotel.count() == 2
        }

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/hotels/${hotel.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = 'updated'
                description = 'updated'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            hotel.refresh()
            assert hotel.name == 'updated'
            assert hotel.description == 'updated'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/hotels/${hotel.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Hotel.count() == 1
        }

        where:
        role          | should    | postStatus | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 201        | 200       | 204
        'ROLE_SELLER' | 'can not' | 403        | 403       | 403
        'ROLE_YH'     | 'can not' | 403        | 403       | 403
        null          | 'can not' | 401        | 401       | 401
    }

}
