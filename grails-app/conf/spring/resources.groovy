import grails.plugin.springsecurity.SpringSecurityService
import zhusu.backend.user.LoginEventListener
import zhusu.backend.user.LoginResponseJsonRender
import zhusu.backend.user.MyUserDetailsService
import zhusu.backend.user.UserEncoderListener

beans = {
    userPasswordEncoderListener(UserEncoderListener)
    userDetailsService(MyUserDetailsService)
    accessTokenJsonRenderer(LoginResponseJsonRender) {
        usernamePropertyName = 'username'
        tokenPropertyName = 'access_token'
        authoritiesPropertyName = 'roles'
        useBearerToken = true
    }
    springSecurityService(SpringSecurityService) {
        authenticationTrustResolver = ref('authenticationTrustResolver')
        grailsApplication = application
        objectDefinitionSource = ref('objectDefinitionSource')
        passwordEncoder = ref('passwordEncoder')
    }
    loginEventListener(LoginEventListener)
}
