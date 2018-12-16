package zhusu.backend

import groovy.util.logging.Slf4j
import org.springframework.boot.info.GitProperties
import zhusu.backend.user.Role
import zhusu.backend.user.User
import zhusu.backend.user.UserService

@Slf4j
class BootStrap {
    GitProperties gitProperties
    UserService userService

    def init = { servletContext ->
        log.info("Application running at commit: {}, branch: {}, commit time: {}, build time: {}"
                , gitProperties.shortCommitId
                , gitProperties.branch
                , gitProperties.commitTime
                , gitProperties.getDate('build.time'))

        environments {
            development {
                Role.validRoles().each {
                    new Role(authority: it).save()
                }
                User user = new User(username: '13572209183', password: 'admin', displayName: 'admin')
                userService.createUserWithRole(user, 'ROLE_ADMIN')
            }
        }
    }
    def destroy = {
    }
}
