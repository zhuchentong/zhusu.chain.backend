package zhusu.backend.operation

import grails.plugin.springsecurity.SpringSecurityService
import zhusu.backend.user.User
import zhusu.backend.util.AliyunOSSUtil

class AliyunOSSController {

    SpringSecurityService springSecurityService

    def getUploadAuthority() {
        User user = springSecurityService.currentUser as User
        respond AliyunOSSUtil.getUploadAuthority(user.username)
    }
}