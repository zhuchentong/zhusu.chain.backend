package zhusu.backend.ota

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Hotel)
abstract class HotelService {

    abstract Hotel get(Serializable id)

    abstract Long count()

    abstract void delete(Serializable id)

    abstract Hotel save(Hotel hotel)

    @Transactional(readOnly = true)
    List<Hotel> list(Map args = [:]) {
        Hotel.createCriteria(args).list {

        }
    }

}
