package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*

import static org.springframework.http.HttpStatus.*

class RoomController extends RestfulController<Room>{

    RoomService roomService
    HotelService hotelService

	static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    RoomController() {
        super(Room)
    }
	
    def index(Integer max, Long hotelId) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        /**
         * 1、根据酒店获取所有房间
         */
        params.hotel = hotelService.get(hotelId)

        PagedResultList result = roomService.list(params)
        respond([RoomList: result, RoomCount: result.totalCount])
    }

    def show(Long id) {
        Room room = roomService.get(id)
        if (!room) {
            render status: NOT_FOUND
        }

        respond room
    }

}
