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

            if (args.lat > 0 && args.lng > 0 && args.distance) {
                sqlRestriction "ST_dwithin(point::geography, ST_GeomFromText('POINT(${args.lat} ${args.lng})', 4326)::geography, ${args.distance * 1000})"
            }
        }
    }

}
