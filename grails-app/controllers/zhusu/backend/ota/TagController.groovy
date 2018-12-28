package zhusu.backend.ota

import grails.gorm.PagedResultList
import grails.rest.*

class TagController extends RestfulController<Tag>{

	static responseFormats = ['json', 'xml']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    TagController() {
        super(Tag)
    }

}
