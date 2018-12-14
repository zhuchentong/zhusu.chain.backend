package zhusu.backend

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class MyDomainServiceSpec extends Specification {

    MyDomainService myDomainService

    void 'test something'() {
        setup:
        myDomainService.save(new MyDomain(kvPair: [key: 'value']
                , strings: ['1', '2'].toArray()
                , location: new GeometryFactory().createPoint(new Coordinate(10, 5))))

        when:
        MyDomain myDomain = MyDomain.list()[0]

        then:
        myDomain.dateCreated
        myDomain.kvPair.key == 'value'
        myDomain.strings == ['1', '2']
        myDomain.location.x == 10
        myDomain.location.y == 5
    }
}
