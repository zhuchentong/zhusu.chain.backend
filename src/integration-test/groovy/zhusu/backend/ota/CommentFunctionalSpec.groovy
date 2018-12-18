package zhusu.backend.ota

import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Specification
import spock.lang.Unroll
import zhusu.backend.user.User
import zhusu.backend.utils.TestUtils

@Integration
@Rollback
class CommentFunctionalSpec extends Specification{

    CommentService commentService
    HotelService hotelService

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#method 可以看到相应评论"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        User testUser1
        User testUser2
        Long userId = 108
        Hotel testHotel1
        Hotel testHotel2
        Long hotelId = 1
        Comment.withNewTransaction {
            testUser1 = TestUtils.createUser('ROLE_YH', '1350000001')
            testUser2 = TestUtils.createUser('ROLE_YH', '1350000002')
            testHotel1 = hotelService.save(
                    new Hotel(name: '北京和颐酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                            description: '4星级酒店', hotelType: 'HOTEL', manager: testUser1, dateCreated: '2018-09-09 12:12:12',
                            englishName: 'BeiJingHeYi', grand: 4, contact: '110'))
            testHotel2 = hotelService.save(
                    new Hotel(name: '北京和颐酒店222', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                            description: '4星级酒店', hotelType: 'HOTEL', manager: testUser2, dateCreated: '2018-09-09 12:12:12',
                            englishName: 'BeiJingHeYi', grand: 4, contact: '110'))

            commentService.save(new Comment(writer: testUser1, hotel: testHotel1, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            commentService.save(new Comment(writer: testUser1, hotel: testHotel2, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            commentService.save(new Comment(writer: testUser2, hotel: testHotel2, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            commentService.save(new Comment(writer: testUser2, hotel: testHotel2, ranking: 2, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            commentService.save(new Comment(writer: testUser2, hotel: testHotel2, ranking: 2, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            commentService.save(new Comment(writer: testUser2, hotel: testHotel2, ranking: 2, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11'))
            userId = testUser1.id
            hotelId = testHotel1.id
        }
        when:
        String param = ''
        if (method == '/listByHotel') {
            param = '?hotelId='+hotelId
        } else if (method == '/listByUser') {
            param = '?userId='+userId
        } else if (method == '/listByRanking') {
            param = '?ranking=3'
        }
        response = rest.get("http://localhost:${serverPort}/api/comments"+method+param) {}

        then:
        response.json.commentCount == count

        where:
        method            | count
        '/'               | 6
        '/listByHotel'    | 1
        '/listByUser'     | 2
        '/listByRanking'  | 3
    }

    @Unroll
    void "#role #should 创建评论"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Comment comment
        Hotel testHotel
        User testUser
        Comment.withNewTransaction {
            testUser = TestUtils.createUser(role, '13500000001')
            testHotel = hotelService.save(
                    new Hotel(name: '北京和颐酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                            description: '4星级酒店', hotelType: 'HOTEL', manager: testUser, dateCreated: '2018-09-09 12:12:12',
                            englishName: 'BeiJingHeYi', grand: 4, contact: '110'))
            comment = new Comment(writer: testUser, hotel: testHotel, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11').save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/comments") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                writer = testUser
                hotel = testHotel
                ranking = 3
                content = 'for test'
                dateCreated = '2019-01-01 12:00:00'
            }
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert Comment.count() == 2
        }

        where:
        role          | should    | postStatus
        'ROLE_ADMIN'  | 'can'     | 201
        'ROLE_SELLER' | 'can'     | 201
        'ROLE_YH'     | 'can'     | 201
        null          | 'can not' | 401
    }

    @Unroll
    void "#role #should 编辑评论"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Comment comment
        Hotel testHotel
        User testUser
        Comment.withNewTransaction {
            testUser = TestUtils.createUser(role, '13500000001')
            testHotel = hotelService.save(
                    new Hotel(name: '北京和颐酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                            description: '4星级酒店', hotelType: 'HOTEL', manager: testUser, dateCreated: '2018-09-09 12:12:12',
                            englishName: 'BeiJingHeYi', grand: 4, contact: '110'))
            comment = new Comment(writer: testUser, hotel: testHotel, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11').save()
        }
        String jwt

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/comments/${comment.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                ranking = 1
                content = 'updated'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            comment.refresh()
            assert comment.ranking == 1
            assert comment.content == 'updated'
        }

        where:
        role          | should    | putStatus
        'ROLE_ADMIN'  | 'can'     | 200
        'ROLE_SELLER' | 'can not' | 403
        'ROLE_YH'     | 'can not' | 403
        null          | 'can not' | 401
    }

    @Unroll
    void "#role #should 删除评论"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Comment comment
        Hotel testHotel
        User testUser
        Comment.withNewTransaction {
            testUser = TestUtils.createUser(role, '13500000001')
            testHotel = hotelService.save(
                    new Hotel(name: '北京和颐酒店', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                            description: '4星级酒店', hotelType: 'HOTEL', manager: testUser, dateCreated: '2018-09-09 12:12:12',
                            englishName: 'BeiJingHeYi', grand: 4, contact: '110'))
            comment = new Comment(writer: testUser, hotel: testHotel, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11').save()
        }
        String jwt

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/comments/${comment.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Comment.count() == 0
        }

        where:
        role          | should    | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 204
        'ROLE_SELLER' | 'can not' | 403
        'ROLE_YH'     | 'can not' | 403
        null          | 'can not' | 401
    }

}
