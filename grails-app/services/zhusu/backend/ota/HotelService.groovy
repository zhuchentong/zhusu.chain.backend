package zhusu.backend.ota

import grails.gorm.services.Service

@Service(Hotel)
abstract class HotelService {

    abstract Hotel get(Serializable id)

    abstract Long count()

    abstract void delete(Serializable id)

    abstract Hotel save(Hotel hotel)

    List<Hotel> list(Map args = [:]) {
        Hotel.createCriteria(args).list {

        }
    }

}
