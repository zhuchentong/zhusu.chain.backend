package zhusu.backend.ota

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*
import grails.gorm.PagedResultList
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.*
import grails.validation.ValidationException
import zhusu.backend.user.User

class HotelController extends RestfulController<Hotel> {

    HotelService hotelService
    SpringSecurityService springSecurityService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    HotelController() {
        super(Hotel)
    }

    def index(Integer max, String name, int minGrand, int maxGrand, double lat, double lng, int distance, String hotelType) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'order'
        params.distance = distance > 0 ?: 3

        /**
         * 名称 | 星级 | 距离 | 日期 | 价格 | 类型
         */
        params.name = name ? "%${name}%" : name
        params.minGrand = minGrand
        params.maxGrand = maxGrand
        params.lat = lat
        params.lng = lng
        params.hotelType = hotelType

        /**
         * 1、管理员与用户可以看到所有酒店列表
         * 2、酒店拥有者只能看到自己管理的酒店
         * 3、管理员拥有对酒店的增删改查权限，其余人只有看的权限
         */

        User currentUser = springSecurityService.currentUser
        if (currentUser && currentUser.hasRole('ROLE_SELLER')) {
            params.manager = currentUser
        }
        PagedResultList result = hotelService.list(params)
        respond([hotelList: result, hotelCount: result.totalCount])
    }

    def show(Long id) {
        Hotel hotel = hotelService.get(id)
        if (!hotel) {
            render status: NOT_FOUND
            return
        }

        respond hotel
    }

    def save() {
        Hotel hotel = new Hotel()
        hotel.properties = request.JSON
        hotel.totalRanking = 0
        hotel.commenterCount = 0
        def lat = request.JSON.position.lat as double
        def lng = request.JSON.position.lng as double
        hotel.point = new GeometryFactory().createPoint(new Coordinate(lat, lng))

        try {
            hotelService.save(hotel)
        } catch (ValidationException e) {
            respond hotel.errors
            return
        }

        render status: CREATED
    }

    def update() {
        Hotel hotel = hotelService.get(params.id as Long)
        hotel.properties = request.JSON
        if (request.JSON.position) {
            def lat = request.JSON.position.lat as double
            def lng = request.JSON.position.lng as double
            hotel.point = new GeometryFactory().createPoint(new Coordinate(lat, lng))
        }

        try {
            hotelService.save(hotel)
        } catch (ValidationException e) {
            respond hotel.errors
            return
        }

        render status: OK
    }

    def delete(Long id) {
        try {
            hotelService.delete(id)
        } catch (DataIntegrityViolationException e) {
            return respond([state: 'error', message: e.cause.cause.message], status: 500)
        }

        render status: NO_CONTENT
    }

}
