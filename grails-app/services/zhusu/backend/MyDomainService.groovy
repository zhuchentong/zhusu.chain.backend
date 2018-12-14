package zhusu.backend

import grails.gorm.services.Service

@Service(MyDomain)
abstract class MyDomainService {

    abstract MyDomain get(Serializable id)

    abstract Long count()

    abstract MyDomain save(MyDomain user)

}
