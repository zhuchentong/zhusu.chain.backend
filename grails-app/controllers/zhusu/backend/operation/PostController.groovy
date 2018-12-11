package zhusu.backend.operation

import grails.gorm.PagedResultList
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.RestfulController
import zhusu.backend.user.User

import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.NOT_FOUND

class PostController extends RestfulController<Post> {

    PostService postService
    SpringSecurityService springSecurityService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: 'DELETE']

    PostController() {
        super(Post)
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        User user = springSecurityService.currentUser
        // 非管理员只能看到发布后的公告列表
        if (null == user || user.hasAnyRole(['ROLE_YH', 'ROLE_SELLER'])) {
            params.published = true
        }

        PagedResultList result = postService.list(params)
        respond([postList: result, postCount: result.totalCount])
    }

    def show(Long id) {
        Post post = postService.get(id)
        if (!post) {
            render status: NOT_FOUND
            return
        }

        if (canBeReadBy(post, springSecurityService.currentUser)) {
            respond post
        } else {
            render status: FORBIDDEN
        }
    }

    private boolean canBeReadBy(Post post, User user) {
        post.published || user?.hasRole('ROLE_ADMIN')
    }

}

