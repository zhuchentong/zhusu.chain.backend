package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.*
import zhusu.backend.user.User

import javax.xml.bind.ValidationException

import static org.springframework.http.HttpStatus.*

class OrderExecutionController extends RestfulController<OrderExecution> {

    OrderExecutionService orderExecutionService
    OrderService orderService
    SpringSecurityService springSecurityService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    OrderExecutionController() {
        super(OrderExecution)
    }

    def index(Integer max, Long orderId) {
        if (!orderId) {
            render status: UNPROCESSABLE_ENTITY
            return
        }
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        User user = springSecurityService.currentUser
        Order order = orderService.get(orderId)
        if (!order) {
            render status: NOT_FOUND
            return
        } else if (canBeReadBy(order, user)) {
            params.myOrder = order
        } else {
            render status: FORBIDDEN
            return
        }
        PagedResultList result = orderExecutionService.list(params)
        respond([orderExecutionList: result, orderExecutionCount: result.totalCount])
    }

    def show(Long id) {
        OrderExecution orderExecution = orderExecutionService.get(id)
        if (!orderExecution) {
            render status: NOT_FOUND
            return
        }

        if (canBeReadBy(orderExecution, springSecurityService.currentUser as User)) {
            respond orderExecution
        } else {
            render status: FORBIDDEN
            return
        }
    }

    def save() {
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.properties = request.JSON

        try {
            orderExecutionService.save(orderExecution)
        } catch (ValidationException e) {
            respond orderExecution.errors
            return
        }

        render status: CREATED
    }

    def last(Long orderId) {
        params.sort = 'dateCreated'
        params.order = 'desc'
        params.limit = 1
        User user = springSecurityService.currentUser
        Order order = orderService.get(orderId)
        if (!order) {
            render status: NOT_FOUND
            return
        } else if (canBeReadBy(order, user)) {
            params.myOrder = order
        } else {
            render status: FORBIDDEN
            return
        }
        PagedResultList result = orderExecutionService.list(params)
        OrderExecution orderExecution = result.get(0) as OrderExecution
        if (orderExecution) {
            respond orderExecution
        } else {
            respond {}
        }
    }

    private boolean canBeReadBy(Order order, User user) {
        (user.hasRole('ROLE_YH') && order.buyer.id == user.id) || (user.hasRole('ROLE_SELLER') && order.room.hotel.manager.id == user.id) || user.hasRole('ROLE_ADMIN')
    }

    private boolean canBeReadBy(OrderExecution orderExecution, User user) {
        (user.hasRole('ROLE_YH') && orderExecution.order.buyer.id == user.id) || (user.hasRole('ROLE_SELLER') && orderExecution.order.room.hotel.manager.id == user.id) || user.hasRole('ROLE_ADMIN')
    }
}
