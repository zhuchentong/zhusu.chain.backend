package zhusu.backend

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import groovy.util.logging.Slf4j
import org.springframework.boot.info.GitProperties
import zhusu.backend.ota.Comment
import zhusu.backend.ota.Hotel
import zhusu.backend.ota.Order
import zhusu.backend.ota.Room
import zhusu.backend.user.Role
import zhusu.backend.user.User
import zhusu.backend.user.UserService

import java.time.LocalDateTime

@Slf4j
class BootStrap {
    GitProperties gitProperties
    UserService userService

    def init = { servletContext ->
        log.info("Application running at commit: {}, branch: {}, commit time: {}, build time: {}"
                , gitProperties.shortCommitId
                , gitProperties.branch
                , gitProperties.commitTime
                , gitProperties.getDate('build.time'))

        environments {
            development {
                Role.validRoles().each {
                    new Role(authority: it).save()
                }
                User user = new User(username: '13572209183', password: 'admin', displayName: 'admin')
                userService.createUserWithRole(user, 'ROLE_ADMIN')
                User user2 = new User(username: '19920001', password: '19920001', displayName: '19920001')
                userService.createUserWithRole(user2, 'ROLE_SELLER')
                User user3 = new User(username: '19920002', password: '19920002', displayName: '19920002')
                userService.createUserWithRole(user3, 'ROLE_YH')

                new Hotel(name: '北京和颐酒店1', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店2', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店3', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店4', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店5', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店6', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店7', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店8', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店9', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店10', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店11', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店12', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店13', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店14', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                new Hotel(name: '北京和颐酒店15', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()
                Hotel hotel = new Hotel(name: '北京和颐酒店16', totalRanking: 123, commenterCount: 49, location: '北京市天安门广场',
                        description: '4星级酒店', hotelType: 'HOTEL', manager: user2, dateCreated: '2018-09-09 12:12:12',
                        englishName: 'BeiJingHeYi', grand: 4, contact: '110', point: new GeometryFactory().createPoint(new Coordinate(10, 5))).save()

                new Comment(writer: user3, hotel: hotel, ranking: 3, content: '我喔喔喔喔', dateCreated: '2018-08-08 11:11:11').save()

                Room room = new Room(name: '标准大床房', hotel: hotel, price: 12345, total: 20, dateCreated: '2018-10-10 11:11:11').save()

                new Order(buyer: user3, room: room, beginDate: LocalDateTime.now(), endDate: LocalDateTime.now()).save()
            }
        }
    }
    def destroy = {
    }
}
