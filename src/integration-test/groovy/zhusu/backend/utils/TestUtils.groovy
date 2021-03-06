package zhusu.backend.utils

import grails.core.GrailsApplication
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.util.Holders
import zhusu.backend.operation.Post
import zhusu.backend.operation.SmsLog
import zhusu.backend.ota.Comment
import zhusu.backend.ota.Hotel
import zhusu.backend.ota.Order
import zhusu.backend.ota.OrderExecution
import zhusu.backend.ota.Room
import zhusu.backend.ota.Tag
import zhusu.backend.user.*

class TestUtils {

    static GrailsApplication grailsApplication = Holders.grailsApplication
    static UserService userService = grailsApplication.mainContext.getBean('userService')

    static String admin = '13500000000'

    static void initEnv() {
        initAllRoles()
        createUser('ROLE_ADMIN', admin)
    }

    static void clearEnv() {
        Post.executeUpdate('delete from Post')
        Comment.executeUpdate('delete from Comment')
        OrderExecution.executeUpdate('delete from OrderExecution')
        Order.executeUpdate('delete from Order')
        Room.executeUpdate('delete from Room')
        Hotel.executeUpdate('delete from Hotel')
        Tag.executeUpdate('delete from Tag')
        SmsLog.executeUpdate('delete from SmsLog')
        LoginHistory.executeUpdate('delete from LoginHistory')
        UserRole.executeUpdate('delete from UserRole')
        Role.executeUpdate('delete from Role')
        User.executeUpdate('delete from User')
    }

    static String login(int serverPort, String user, String pwd) {
        String loginEndpointUrl = grailsApplication.config.grails.plugin.springsecurity.rest.login.endpointUrl
        String loginUrl = "http://localhost:${serverPort}${loginEndpointUrl}"
        RestBuilder restBuilder = new RestBuilder()

        RestResponse response = restBuilder.post(loginUrl) {
            json {
                username = user
                password = pwd
            }
        }

        if (response.status == 200) {
            return response.json.access_token
        } else {
            throw new RuntimeException("Login failed for user ${user} with password ${pwd}.")
        }
    }

    static void initAllRoles() {
        Role.validRoles().each {
            new Role(authority: it).save()
        }
    }

    static User createUser(String role, String name) {
        User user = new User(username: name, password: name, displayName: name)
        userService.createUserWithRole(user, role)
        user
    }

    static User createUser(List<String> roles, String name) {
        User user = new User(username: name, password: name, displayName: name)
        userService.createUserWithRoles(user, roles)
        user
    }

}
