package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*
import grails.validation.ValidationException
import zhusu.backend.user.User
import zhusu.backend.user.UserService

import static org.springframework.http.HttpStatus.*

class CommentController extends RestfulController<Comment>{

    CommentService commentService
    HotelService hotelService
    UserService userService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    CommentController() {
        super(Comment)
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10 ,100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        PagedResultList result = commentService.list(params)
        respond([commentList: result, commentCount: result.totalCount])
    }

    def show(Long id) {
        Comment comment = commentService.get(id)
        if (!comment) {
            render status: NOT_FOUND
            return
        }
        respond comment
    }

    def save() {
        Comment comment = new Comment()
        comment.properties = request.JSON

        try {
            commentService.save(comment)
        } catch (ValidationException e) {
            respond comment.errors
            return
        }

        render status: CREATED
    }

    def listByHotel(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId)
        List result = commentService.findAllByHotel(hotel)
        respond([commentList: result, commentCount: result.size()])
    }

    def listByUser(Long userId) {
        User user = userService.get(userId)
        List result = commentService.findAllByWriter(user)
        respond([commentList: result, commentCount: result.size()])
    }

    def listByRanking(int ranking) {
        params.ranking = ranking
        PagedResultList result = commentService.list(params)
        respond([commentList: result, commentCount: result.totalCount])
    }

}
