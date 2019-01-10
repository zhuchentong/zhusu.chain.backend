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

import java.time.LocalDateTime

@Integration
@Rollback
class OrderFunctionalSpec extends Specification {

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#role 可以访问其相关订单列表及详情"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Order order
        Order.withNewTransaction {
            TestUtils.createUser('ROLE_ADMIN', '13500000001')
            User commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User commonUser2 = TestUtils.createUser('ROLE_YH', '1992002')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            Room room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: commonUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            new Order(buyer: commonUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()

            new Order(buyer: commonUser2, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
        }

        when:
        String jwt
        if (role) {
            jwt = TestUtils.login(serverPort, username, username)
        }
        response = rest.get("http://localhost:${serverPort}/api/orders") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.orderCount == count

        where:
        role          | count | username
        'ROLE_ADMIN'  | 3     | '13500000001'
        'ROLE_SELLER' | 3     | '1992003'
        'ROLE_YH'     | 2     | '1992001'
        null          | null  | ''
    }

    @Unroll
    void "#role #should 创建订单"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        User commonUser
        Order order
        Room myRoom
        Order.withNewTransaction {
            currentUser = TestUtils.createUser(role, '13500000001')
            commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            myRoom = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: commonUser, room: myRoom, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/orders") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                room {
                    id = myRoom.id
                }
                buyer {
                    id = commonUser.id
                }
                beginDate = '2018-09-09T13:13:12'
                endDate = '2018-09-09T13:13:13'
                attributes {}
            }
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert Order.count() == 2
        }

        where:
        role          | should    | postStatus
        'ROLE_ADMIN'  | 'can not' | 403
        'ROLE_SELLER' | 'can not' | 403
        'ROLE_YH'     | 'can'     | 201
        null          | 'can not' | 401
    }

    @Unroll
    void "连续日期不会重复计算住房"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Room myRoom
        User commonUser
        Order.withNewTransaction {
            commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            myRoom = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 2, dateCreated: '2018-10-10 11:11:11').save()
            new Order(buyer: commonUser, room: myRoom, beginDate: '2018-12-12 00:00:00', endDate: '2018-12-15 00:00:00').save()
        }
        String jwt

        when:
        jwt = TestUtils.login(serverPort, '1992001', '1992001')
        response = rest.post("http://localhost:${serverPort}/api/orders") {
            header('Authorization', "Bearer ${jwt}")
            json {
                room {
                    id = myRoom.id
                }
                buyer {
                    id = commonUser.id
                }
                beginDate = '2018-12-15 00:00:00'
                endDate = '2018-12-19 00:00:00'
                attributes {}
            }
        }

        then:
        response.status == 201

        when:
        response = rest.post("http://localhost:${serverPort}/api/orders") {
            header('Authorization', "Bearer ${jwt}")
            json {
                room {
                    id = myRoom.id
                }
                buyer {
                    id = commonUser.id
                }
                beginDate = '2018-12-12 00:00:00'
                endDate = '2018-12-19 00:00:00'
                attributes {}
            }
        }

        then:
        response.status == 201

        when:
        response = rest.post("http://localhost:${serverPort}/api/orders") {
            header('Authorization', "Bearer ${jwt}")
            json {
                room {
                    id = myRoom.id
                }
                buyer {
                    id = commonUser.id
                }
                beginDate = '2018-12-12 00:00:00'
                endDate = '2018-12-19 00:00:00'
                attributes {}
            }
        }

        then:
        response.status == 403
    }

    @Unroll
    void "#role #should 编辑、删除订单"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        User commonUser
        Order order
        Room room
        Order.withNewTransaction {
            currentUser = TestUtils.createUser(role, '13500000001')
            commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: commonUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
        }
        String jwt

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/orders/${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                status = 'CONFIRMED'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            order.refresh()
            assert order.status == 'CONFIRMED'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/orders/${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Order.count() == 0
        }

        where:
        role          | should    | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 200       | 204
        'ROLE_SELLER' | 'can not' | 403       | 403
        'ROLE_YH'     | 'can not' | 403       | 403
        null          | 'can not' | 401       | 401
    }

    void "#role #should 更改订单状态"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        User commonUser
        Order order
        Room room
        Order.withNewTransaction {
            currentUser = TestUtils.createUser(role, '13500000001')
            commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: currentUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            new OrderExecution(order: order, status: 'CREATED', operator: currentUser, memo: 'test1', dateCreated: '2018-12-12 11:11:11').save()
        }
        String jwt

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/orders/pay?id=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            order.refresh()
            List<OrderExecution> orderExecutionList = OrderExecution.findAllByOrder(order)
            assert order.status == 'PAID'
            assert orderExecutionList.size() == 2
        }

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/orders/confirm?id=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            order.refresh()
            List<OrderExecution> orderExecutionList = OrderExecution.findAllByOrder(order)
            assert order.status == 'CONFIRMED'
            assert orderExecutionList.size() == 3
        }

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/orders/checkIn?id=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            order.refresh()
            List<OrderExecution> orderExecutionList = OrderExecution.findAllByOrder(order)
            assert order.status == 'CHECKIN'
            assert orderExecutionList.size() == 4
        }

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/orders/checkOut?id=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            order.refresh()
            List<OrderExecution> orderExecutionList = OrderExecution.findAllByOrder(order)
            assert order.status == 'CHECKOUT'
            assert orderExecutionList.size() == 5
        }

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/orders/cancel?id=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            order.refresh()
            List<OrderExecution> orderExecutionList = OrderExecution.findAllByOrder(order)
            assert order.status == 'CANCELED'
            assert orderExecutionList.size() == 6
        }

        where:
        role          | should    | status
        'ROLE_ADMIN'  | 'can'     | 200
        'ROLE_SELLER' | 'can'     | 200
        'ROLE_YH'     | 'can'     | 200
        null          | 'can not' | 401
    }

}
