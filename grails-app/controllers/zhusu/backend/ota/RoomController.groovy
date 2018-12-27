package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*
import grails.validation.ValidationException

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
        if (hotelId) {
            List roomList = roomService.findAllByHotel(hotelService.get(hotelId))
            respond([roomList: roomList, roomCount: roomList.size()])
        } else {
            PagedResultList result = roomService.list(params)
            respond([roomList: result, roomCount: result.totalCount])
        }
    }

    def show(Long id) {
        Room room = roomService.get(id)
        if (!room) {
            render status: NOT_FOUND
        }

        respond room
    }

    def save() {
        Room room = new Room()
        room.properties = request.JSON

        try {
            roomService.save(room)
        } catch (ValidationException e) {
            respond room.errors
            return
        }

        render status: CREATED
    }

}
