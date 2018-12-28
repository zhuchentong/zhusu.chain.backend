package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.PagedResultList
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.*
import org.springframework.beans.factory.annotation.Qualifier
import zhusu.backend.user.User

import javax.xml.bind.ValidationException
import java.time.LocalDateTime

import static org.springframework.http.HttpStatus.*

class OrderController extends RestfulController<Order>{

    OrderService orderService
    HotelService hotelService
    RoomService roomService
    SpringSecurityService springSecurityService

    @Qualifier('localDateTimeValueConverter')
    ValueConverter localDateTimeValueConverter

	static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    OrderController() {
        super(Order)
    }
	
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        User user = springSecurityService.currentUser as User

        if (user.hasRole('ROLE_YH')) {
            // 普通用户只能看到自己消费的订单
            params.user = user
        } else if (user.hasRole('ROLE_SELLER')) {
            // 酒店管理员只能看到隶属于本酒店的订单
            List<Hotel> myHotels = hotelService.findAllByManager(user)
            List<Room> rooms = new ArrayList<>()
            myHotels.each{
                rooms.addAll(roomService.findAllByHotel(it))
            }
            params.rooms = rooms
        }

        PagedResultList result = orderService.list(params)
        respond([orderList: result, orderCount: result.totalCount])
    }

    def show(Long id) {
        Order order = orderService.get(id)
        if (!order) {
            render status: NOT_FOUND
        }

        if (canBeReadBy(order, springSecurityService.currentUser as User)) {
            respond order
        } else {
            render status: FORBIDDEN
        }
    }

    def save() {
        Order order = new Order()
        order.properties = request.JSON
        User user = springSecurityService.currentUser as User
        if (user.hasRole('ROLE_YH')) {
            order.setBuyer(user)
        } else {
            render status: FORBIDDEN
        }
        Map<String, String> attributes = new HashMap<>()
        attributes.put('roomCount', request.JSON.attributes.roomCount as String)
        attributes.put('personName', request.JSON.attributes.personName as String)
        attributes.put('telephone', request.JSON.attributes.telephone as String)
        attributes.put('email', request.JSON.attributes.email as String)
        attributes.put('arrivalTime', request.JSON.attributes.arrivalTime as String)
        order.setAttributes(attributes)

        try {
            orderService.save(order)
            OrderExecution orderExecution = new OrderExecution()
            orderExecution.setOrder(order)
            orderExecution.setStatus('CREATED')
            orderExecution.setOperator(springSecurityService.currentUser as User)
            orderExecution.save()
        } catch(ValidationException e) {
            respond order.errors
            return
        }

        render status: CREATED
    }

    /**
     * 创建阶段进入已确认阶段
     * @param id
     */
    def confirm(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CREATED') {
            orderService.confirm(order, springSecurityService.currentUser as User)
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 已确认阶段进入入住阶段
     * @param id
     */
    def checkIn(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CONFIRMED') {
            orderService.checkIn(order, springSecurityService.currentUser as User)
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 入住阶段进入离店阶段
     * @param id
     */
    def checkOut(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CHECKIN') {
            orderService.checkOut(order, springSecurityService.currentUser as User)
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 取消订单
     * @param id
     */
    def cancel(Long id) {
        Order order = orderService.get(id)
        if (order.status != 'CANCELED') {
            orderService.cancel(order, springSecurityService.currentUser as User)
        } else {
            render status: FORBIDDEN
        }
    }

    private boolean canBeOrderBy(String startDate, String endDate, Room room, int count) {
        params.startDate = startDate
        params.endDate = endDate
        params.room = room

        if (startDate && endDate && room) {
            PagedResultList result = orderService.list(params)
            if (room.total > result.totalCount + count) {
                true
            }
        }
        false
    }

    private static boolean canBeReadBy(Order order, User user) {
        if (user.hasRole('ROLE_YH')) {
            // 普通用户只能看到自己消费的订单
            if (order.buyer == user) {
                true
            } else {
                false
            }
        } else if (user.hasRole('ROLE_SELLER')) {
            // 酒店管理员只能看到隶属于本酒店的订单
            if (order.room.hotel.manager == user) {
                true
            } else {
                false
            }
        }
    }
}
