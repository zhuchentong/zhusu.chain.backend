package zhusu.backend.ota

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Tag)
abstract class TagService {

    abstract Tag get(Long id)

    abstract Long count()

    abstract void delete(Long id)

    abstract Tag save(Tag tag)

    @Transactional
    List<Tag> list(Map args = [:]) {
        Tag.createCriteria().list(args) {

        }
    }

}
