package zhusu.backend.user

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    String displayName
    String idCard
    String ethAccount
    boolean enabled = true
    boolean passwordExpired = false
    boolean accountExpired = false
    boolean accountLocked = false
    LocalDateTime dateCreated

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    boolean hasRole(String role) {
        authorities.collect { it.authority }.contains(role)
    }

    boolean hasAnyRole(List<String> role) {
        !authorities.collect { it.authority }.intersect(role as Set<String>).empty
    }

    static constraints = {
        username nullable: false, blank: false, maxSize: 11, unique: true
        password nullable: false, blank: false, password: true
        displayName nullable: false, blank: false, maxSize: 30, unique: true
        idCard nullable: true
        ethAccount nullable: true, size: 40..40
    }

    static mapping = {
        comment '用户'
        table 'myuser'
        dynamicUpdate true
        username comment: '用户名（手机）', index: 'idx_user_username'
        password column: 'user_password', comment: '密码'
        displayName comment: '昵称'
        enabled comment: '是否有效'
        idCard comment: '身份证号'
        ethAccount comment: '用户以太坊账户', index: 'idx_user_eth_account'
        passwordExpired comment: '密码过期'
        dateCreated comment: '创建时间', index: 'idx_date_created'
    }

    static transients = ['accountExpired', 'accountLocked']
}
