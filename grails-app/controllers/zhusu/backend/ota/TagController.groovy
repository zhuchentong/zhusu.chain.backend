package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*

class TagController extends RestfulController<Tag>{

    TagService tagService

	static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    TagController() {
        super(Tag)
    }
	
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'id'
        params.order = params.order ?: 'desc'

        PagedResultList result = tagService.list(params)
        respond([tagList: result, tagCount: result.totalCount])
    }

}
