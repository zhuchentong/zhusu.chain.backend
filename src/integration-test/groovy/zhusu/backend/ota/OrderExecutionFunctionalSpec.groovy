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
class OrderExecutionFunctionalSpec extends Specification{

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#role 可以访问其相关的订单执行列表及详情"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        OrderExecution orderExecution
        Order order
        OrderExecution.withNewTransaction {
            TestUtils.createUser('ROLE_ADMIN', '13500000001')
            User commonUser = TestUtils.createUser('ROLE_YH', '1992001')
            User commonUser2 = TestUtils.createUser('ROLE_YH', '1992002')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            Room room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: commonUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            orderExecution = new OrderExecution(order: order, status: 'CREATED', operator: commonUser).save()
            OrderExecution orderExecution2 = new OrderExecution(order: order, status: 'CONFIRMED', operator: commonUser).save()

            Order order2 = new Order(buyer: commonUser2, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            OrderExecution orderExecution3 = new OrderExecution(order: order2, status: 'CREATED', operator: commonUser2).save()
        }

        when:
        String jwt
        if(role) {
            jwt = TestUtils.login(serverPort, username, username)
        }
        response = rest.get("http://localhost:${serverPort}/api/orderExecutions?orderId=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.orderExecutionCount == count
        response.status == status

        when:
        response = rest.get("http://localhost:${serverPort}/api/orderExecutions/${orderExecution.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }
        then:
        response.status == detailStatus
        where:
        role          | status     | count  | username      | detailStatus
        'ROLE_ADMIN'  | 200        | 2      | '13500000001' | 200
        'ROLE_SELLER' | 200        | 2      | '1992003'     | 200
        'ROLE_YH'     | 200        | 2      | '1992001'     | 200
        'ROLE_YH'     | 403        | null   | '1992002'     | 403
        null          | 401        | null   | 'null'        | 401
    }

    @Unroll
    void "#role #should 创建、删除、编辑订单执行步骤"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        OrderExecution orderExecution
        Order order
        OrderExecution.withNewTransaction {
            TestUtils.createUser(role, '13500000001')
            currentUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            Room room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: currentUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            orderExecution = new OrderExecution(order: order, status: 'CREATED', operator: currentUser).save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/orderExecutions") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json "{ order: { id: ${order.id} }, status: 'CREATED', operator: { id: ${currentUser.id} } }"
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert OrderExecution.count() == 2
        }

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/orderExecutions/${orderExecution.id}") {
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
            orderExecution.refresh()
            assert orderExecution.status == 'CONFIRMED'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/orderExecutions/${orderExecution.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert OrderExecution.count() == 1
        }

        where:
        role          | should    | postStatus | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 201        | 200       | 204
        'ROLE_SELLER' | 'can not' | 403        | 403       | 403
        'ROLE_YH'     | 'can not' | 403        | 403       | 403
        null          | 'can not' | 401        | 401       | 401
    }

    void "#role 使用 last() 方法获取订单最新状态"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User currentUser
        OrderExecution orderExecution
        Order order
        OrderExecution.withNewTransaction {
            TestUtils.createUser(role, '13500000001')
            currentUser = TestUtils.createUser('ROLE_YH', '1992001')
            User managerUser = TestUtils.createUser('ROLE_SELLER', '1992003')
            Hotel hotel = new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                    description: '4星级酒店', hotelType: 'HOTEL', manager: managerUser, dateCreated: '2018-09-09 12:12:12',
                    englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
            Room room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()
            order = new Order(buyer: currentUser, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            orderExecution = new OrderExecution(order: order, status: 'CREATED', operator: currentUser, memo: 'test1', dateCreated: '2018-12-12 11:11:11').save()
            orderExecution = new OrderExecution(order: order, status: 'CREATED', operator: currentUser, memo: 'test2', dateCreated: '2018-12-21 11:11:11').save()
        }
        String jwt

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, username, username)
        }
        response = rest.get("http://localhost:${serverPort}/api/orderExecutions/last?orderId=${order.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status
        if (status == 200) {
            assert response.json.memo == 'test2'
        }

        where:
        role          | status      | username
        'ROLE_ADMIN'  | 200         | '13500000001'
        'ROLE_SELLER' | 200         | '1992003'
        'ROLE_YH'     | 200         | '1992001'
        null          | 401         | ''
    }

}
