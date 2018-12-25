package zhusu.backend.ota

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Room)
abstract class RoomService {

    abstract Room get(Serializable id)

    abstract Long count()

    abstract void delete(Serializable id)

    abstract Room save(Room room)

    abstract List<Room> findAllByHotel(Hotel hotel)

    @Transactional(readOnly = true)
    List<Room> list(Map args = [:]) {
        Room.createCriteria().list(args) {

            if (args.hotel) {
                eq('hotel', args.hotel)
            }

        }
    }

}
