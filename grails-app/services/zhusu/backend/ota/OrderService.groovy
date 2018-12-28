package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import zhusu.backend.user.User

@Service(Order)
abstract class OrderService {

    @Autowired
    @Qualifier('localDateTimeValueConverter')
    ValueConverter localDateTimeValueConverter

    abstract Order get(Long id)

    abstract Long count()

    abstract void delete(Long id)

    abstract Order save(Order order)

    @Transactional
    void confirm(Order order, User user) {
        order.setStatus('CONFIRMED')
        order.save()
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CONFIRMED')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

    @Transactional
    void checkIn(Order order, User user) {
        order.setStatus('CHECKIN')
        order.save()
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CHECKIN')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

    @Transactional
    void checkOut(Order order, User user) {
        order.setStatus('CHECKOUT')
        order.save()
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CHECKOUT')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

    @Transactional
    void cancel(Order order, User user) {
        order.setStatus('CANCELED')
        order.save()
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CANCELED')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

    @Transactional(readOnly = true)
    List<Order> list(Map args = [:]) {
        Order.createCriteria().list(args) {
            if (args.user) {
                eq('buyer', args.user)
            }
            if (args.rooms) {
                'in'('room', args.rooms)
            }

            if (args.startDate && args.endDate && args.room) {
                and {
                    or {
                        and {
                            lte('beginDate', localDateTimeValueConverter.convert(args.startDate))
                            gt('endDate', localDateTimeValueConverter.convert(args.startDate))
                        }

                        and {
                            lte('beginDate', localDateTimeValueConverter.convert(args.endDate))
                            gte('endDate', localDateTimeValueConverter.convert(args.endDate))
                        }

                        and {
                            gt('beginDate', localDateTimeValueConverter.convert(args.startDate))
                            lte('endDate', localDateTimeValueConverter.convert(args.endDate))
                        }
                    }
                    eq('room', args.room)
                }
            }
        }
    }

}
