package zhusu.backend.ota

import grails.databinding.converters.ValueConverter
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import zhusu.backend.user.User

@Service(Comment)
abstract class CommentService {

    @Autowired
    @Qualifier('localDateTimeValueConverter')
    ValueConverter localDateTimeValueConverter

    abstract Comment get(Serializable id)

    abstract Long count()

    abstract void delete(Serializable id)

    abstract Comment save(Comment comment)

    abstract List<Comment> findAllByHotel(Hotel hotel)

    abstract List<Comment> findAllByWriter(User writer)

    @Transactional(readOnly = true)
    List<Comment> list(Map args = [:]) {
        Comment.createCriteria().list(args) {
            if (args.ranking) {
                eq('ranking', args.ranking)
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
