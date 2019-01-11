package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.hibernate.Session
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
    void order(Order order, User user) {
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('CREATED')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

    @Transactional
    void pay(Order order, User user) {
        order.setStatus('PAID')
        order.save()
        OrderExecution orderExecution = new OrderExecution()
        orderExecution.setOrder(order)
        orderExecution.setStatus('PAID')
        orderExecution.setOperator(user)
        orderExecution.save()
    }

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
        }
    }

    @Transactional(readOnly = true)
    int orderCounts(Map args = [:]) {
        final String sql = """
          select d.datetime, count(*) 
          from myorder o 
            left join (select generate_series(to_date('${args.beginDate}', 'yyyy-MM-dd'), to_date('${args.endDate}', 'yyyy-MM-dd'), '1 day') datetime) d 
            on d.datetime >= o.begin_date and d.datetime < o.end_date 
          where o.room_id = ${args.roomId} 
            group by d.datetime 
            order by count(*) desc;
        """.stripIndent()
        List<Object> list = new ArrayList()
        Order.withSession { Session session ->
            list = session.createNativeQuery(sql).list()
        }
        if (list.size() > 0) {
            if (null != list.get(0)[0]) {
                list.get(0)[1]
            } else {
                0
            }
        } else {
            65533
        }
    }

}
