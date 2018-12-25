package zhusu.backend.ota

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(OrderExecution)
abstract class OrderExecutionService {

    abstract OrderExecution get(Long id)

    abstract Long count()

    abstract void delete(Long id)

    abstract OrderExecution save(OrderExecution orderExecution)

    @Transactional(readOnly = true)
    List<OrderExecution> list(Map args = [:]) {
        OrderExecution.createCriteria().list(args) {
            if (args.myOrder) {
                eq('order', args.myOrder)
            }
        }
    }

}
