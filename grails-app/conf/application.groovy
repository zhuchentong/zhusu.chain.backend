grails.gorm.default.mapping = {
    id generator: 'identity'
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'zhusu.backend.user.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'zhusu.backend.user.UserRole'
grails.plugin.springsecurity.authority.className = 'zhusu.backend.user.Role'

// 监听登录事件，记录登录日志
grails.plugin.springsecurity.useSecurityEventListener = true

grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = false

grails.plugin.springsecurity.rest.token.storage.jwt.with {
    useSignedJwt = System.getenv('USE_SIGNED_JWT') ?: true
    secret = System.getenv('JWT_SECRET') ?: 'qrD6h8K6S9503Q06Y6Rfk21TErImPYqa'
    expiration = System.getenv('JWT_EXPIRATION') ?: 3600
    useEncryptedJwt = System.getenv('USE_ENCRYPTED_JWT') ?: false
    privateKeyPath = System.getenv('PRIVATE_KEY_PATH') ?: ''
    publicKeyPath = System.getenv('PUBLIC_KEY_PATH') ?: ''
}

grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/', access: ['permitAll']]
        , [pattern: '/error', access: ['permitAll']]
        , [pattern: '/api/sendSmsCode', access: ['permitAll']]
        , [pattern: '/api/register', access: ['permitAll']]
        , [pattern: '/api/self', access: ["isFullyAuthenticated()"]]
        , [pattern: '/api/updatePersonalInfo', access: ["isFullyAuthenticated()"]]
        , [pattern: '/api/changePassword', access: ["isFullyAuthenticated()"]]
        , [pattern: '/api/users', access: ['ROLE_ADMIN']]
        , [pattern: '/api/users/resetPassword', access: ['permitAll'], httpMethod: 'PUT']
        , [pattern: '/api/users/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/smslogs', access: ['ROLE_ADMIN'], httpMethod: 'GET']
        , [pattern: '/api/getUploadAuthority', access: ['isFullyAuthenticated()'], httpMethod: 'GET']
        , [pattern: '/api/posts', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/posts', access: ['ROLE_ADMIN']]
        , [pattern: '/api/posts/*', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/posts/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/comments', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/comments', access: ['isFullyAuthenticated()'], httpMethod: 'POST']
        , [pattern: '/api/comments/*', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/comments/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/hotels', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/hotels', access: ['ROLE_ADMIN']]
        , [pattern: '/api/hotels/*', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/hotels/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/rooms', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/rooms', access: ['ROLE_ADMIN']]
        , [pattern: '/api/rooms/*', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/rooms/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/tags', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/tags', access: ['ROLE_ADMIN']]
        , [pattern: '/api/tags/*', access: ['permitAll'], httpMethod: 'GET']
        , [pattern: '/api/tags/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/orders', access: ['isFullyAuthenticated()']]
        , [pattern: '/api/orders/*', access: ['isFullyAuthenticated()'], httpMethod: 'GET']
        , [pattern: '/api/orders/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/orderExecutions', access: ['isFullyAuthenticated()'], httpMethod: 'GET']
        , [pattern: '/api/orderExecutions', access: ['ROLE_ADMIN']]
        , [pattern: '/api/orderExecutions/*', access: ['isFullyAuthenticated()'], httpMethod: 'GET']
        , [pattern: '/api/orderExecutions/*', access: ['ROLE_ADMIN']]
        , [pattern: '/api/**', access: ['denyAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/api/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter']
]

grails.plugin.springsecurity.failureHandler.exceptionMappings = [
        [exception: org.springframework.security.authentication.CredentialsExpiredException.name, url: '/api/passwordExpired']
]

grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true
