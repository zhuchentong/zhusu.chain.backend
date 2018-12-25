package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.PagedResultList
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.*
import org.springframework.beans.factory.annotation.Qualifier
import zhusu.backend.user.User

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
    /**
     * 预定房间
     */
    @Transactional
    def order(Long roomId, String startDate, String endDate, int roomCount, String personName, Long telephone, String email, String arrivalTime) {
        Order order = new Order()
        Room room = roomService.get(roomId)
        if (!room || canBeOrderBy(startDate, endDate, room, roomCount)) {
            render status: FORBIDDEN
        }
        order.setRoom(room)
        order.setBeginDate(localDateTimeValueConverter.convert(startDate) as LocalDateTime)
        order.setEndDate(localDateTimeValueConverter.convert(endDate) as LocalDateTime)
        User user = springSecurityService.currentUser as User
        if (user.hasRole('ROLE_YH')) {
            order.setBuyer(user)
        } else {
            render status: FORBIDDEN
        }
        Map<String, String> attributes = new HashMap<>()
        attributes.put('roomCount', roomCount.toString())
        attributes.put('personName', personName)
        attributes.put('telephone', telephone.toString())
        attributes.put('email', email)
        attributes.put('arrivalTime', arrivalTime)
        order.setAttributes(attributes)
        order.save()

        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CREATED')
        orderExecution.setOperator(springSecurityService.currentUser as User)
        orderExecution.save()
    }

    /**
     * 创建阶段进入已确认阶段
     * @param id
     */
    @Transactional
    def confirm(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CREATED') {
            order.setStatus('CONFIRMED')
            order.save()
            OrderExecution orderExecution = new OrderExecution()
            orderExecution.setOrder(order)
            orderExecution.setStatus('CONFIRMED')
            orderExecution.setOperator(springSecurityService.currentUser as User)
            orderExecution.save()
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 已确认阶段进入入住阶段
     * @param id
     */
    @Transactional
    def checkIn(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CONFIRMED') {
            order.setStatus('CHECKIN')
            order.save()
            OrderExecution orderExecution = new OrderExecution()
            orderExecution.setOrder(order)
            orderExecution.setStatus('CHECKIN')
            orderExecution.setOperator(springSecurityService.currentUser as User)
            orderExecution.save()
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 入住阶段进入离店阶段
     * @param id
     */
    @Transactional
    def checkOut(Long id) {
        Order order = orderService.get(id)
        if (order.status == 'CHECKIN') {
            order.setStatus('CHECKOUT')
            order.save()
            OrderExecution orderExecution = new OrderExecution()
            orderExecution.setOrder(order)
            orderExecution.setStatus('CHECKOUT')
            orderExecution.setOperator(springSecurityService.currentUser as User)
            orderExecution.save()
        } else {
            render status: FORBIDDEN
        }
    }

    /**
     * 取消订单
     * @param id
     */
    @Transactional
    def cancel(Long id) {
        Order order = orderService.get(id)
        if (order.status != 'CANCEL') {
            order.setStatus('CANCEL')
            order.save()
            OrderExecution orderExecution = new OrderExecution()
            orderExecution.setOrder(order)
            orderExecution.setStatus('CANCEL')
            orderExecution.setOperator(springSecurityService.currentUser as User)
            orderExecution.save()
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
