package zhusu.backend.operation

import grails.databinding.converters.ValueConverter
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

@Service(Post)
abstract class PostService {

    @Autowired
    @Qualifier('localDateTimeValueConverter')
    ValueConverter localDateTimeValueConverter

    abstract Post get(Serializable id)

    abstract Long count()

    abstract void delete(Serializable id)

    abstract Post save(Post post)

    @Transactional(readOnly = true)
    List<Post> list(Map args = [:]) {
        Post.createCriteria().list(args) {
            if (null != args.published && '' != args.published) {
                eq('published', args.published)
            }

            if (args.startTime) {
                gte('dateCreated', localDateTimeValueConverter.convert(args.startTime))
            }

            if (args.endTime) {
                lte('dateCreated', localDateTimeValueConverter.convert(args.endTime))
            }
        }
    }

}
