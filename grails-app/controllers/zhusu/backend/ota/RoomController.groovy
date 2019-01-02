package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*

class RoomController extends RestfulController<Room> {

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

    def delete(Long id) {
        try {
            roomService.delete(id)
        } catch (DataIntegrityViolationException e) {
            return handleDataIntegrityViolationException(e, id)
        }

        render status: NO_CONTENT
    }

    void handleDataIntegrityViolationException(DataIntegrityViolationException e, Long id) {
        String tableName = "unknown"
        String detailMessage = e.cause.cause.message.find(/is still referenced from table ".*"./)
        if (detailMessage.length() > 0) {
            tableName = detailMessage[32..detailMessage.length() - 3]
        }
        String message = "表 ${tableName} 中存在对数据 [id = ${id}] 的引用，请处理引用后尝试删除。"
        respond([state: 'error', message: message, data: e.stackTrace], status: 500)
    }

}
