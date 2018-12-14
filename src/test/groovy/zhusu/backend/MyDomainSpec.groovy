package zhusu.backend

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import grails.test.hibernate.HibernateSpec

class MyDomainSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [MyDomain] }

    void "test something"() {
        setup:
        new MyDomain(kvPair: [key: 'value']
                , strings: ['1', '2'].toArray()
                , location: new GeometryFactory().createPoint(new Coordinate(10, 5))).save(flush: true)

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
