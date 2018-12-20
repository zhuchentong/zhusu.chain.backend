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
        Hotel.createCriteria().list(args) {
            if (args.name) {
                like('name', args.name)
            }

            if (args.minGrand) {
                gte('grand', args.minGrand)
            }

            if (args.maxGrand) {
                lte('grand', args.maxGrand)
            }

            if (args.manager) {
                eq('manager', args.manager)
            }

            if (args.hotelType == 'HOTEL' || args.hotelType == 'HOMESTAY') {
                eq('hotelType', args.hotelType)
            }

            if (args.point && args.distance) {
                lte {
                    args.point.distance('point')
                    args.distance
                }
            }
        }
    }

}
