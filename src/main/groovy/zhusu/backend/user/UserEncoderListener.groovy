package zhusu.backend.user

import grails.events.annotation.gorm.Listener
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PreInsertEvent
import org.grails.datastore.mapping.engine.event.PreUpdateEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import zhusu.backend.util.CipherUtil

@CompileStatic
class UserEncoderListener {

    @Value('${des_key}')
    String key

    @Autowired
    SpringSecurityService springSecurityService

    @Listener(User)
    void onPreInsertEvent(PreInsertEvent event) {
        encodeForEvent(event)
    }

    @Listener(User)
    void onPreUpdateEvent(PreUpdateEvent event) {
        encodeForEvent(event)
    }

    private void encodeForEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof User) {
            User u = event.entityObject as User
            if (u.password && ((event instanceof PreInsertEvent) || (event instanceof PreUpdateEvent && u.isDirty('password')))) {
                event.getEntityAccess().setProperty('password', encodePassword(u.password))
            }

            if (u.idCard && ((event instanceof PreInsertEvent) || (event instanceof PreUpdateEvent && u.isDirty('idCard')))) {
                event.getEntityAccess().setProperty('idCard', encodeIdCard(u.idCard))
            }
        }
    }

    private String encodePassword(String password) {
        springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    private String encodeIdCard(String idCard) {
        CipherUtil.desEncrypt(idCard, key)
    }
}
