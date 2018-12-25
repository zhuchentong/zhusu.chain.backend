package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

@Service(Order)
abstract class OrderService {

    @Autowired
    @Qualifier('localDateTimeValueConverter')
    ValueConverter localDateTimeValueConverter

    abstract Order get(Long id)

    abstract Long count()

    abstract void delete(Long id)

    abstract Order save(Order order)

    @Transactional(readOnly = true)
    List<Order> list(Map args = [:]) {
        Order.createCriteria().list(args) {
            if (args.user) {
                eq('user', args.user)
            }
            if (args.rooms) {
                eqAll('room', args.rooms)
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
