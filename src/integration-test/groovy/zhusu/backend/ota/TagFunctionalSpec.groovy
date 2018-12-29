package zhusu.backend.ota

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification
import spock.lang.Unroll
import zhusu.backend.utils.TestUtils

@Integration
@Rollback
class TagFunctionalSpec extends Specification {

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#role 可以访问标签列表并访问标签详情"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Tag tag
        Tag.withNewTransaction {
            if (role) {
                TestUtils.createUser(role, '13500000001')
            }
            tag = new Tag(name: 'test1').save()
            new Tag(name: 'test2').save()
        }

        when:
        String jwt
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/tags") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.getProperties().size() == count

        when:
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/tags/${tag.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status

        where:
        role          | status | count
        'ROLE_ADMIN'  | 200    | 2
        'ROLE_SELLER' | 200    | 2
        'ROLE_YH'     | 200    | 2
        null          | 200    | 2
    }

    @Unroll
    void "#role #should 创建、编辑、删除标签"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Tag tag
        Tag.withNewTransaction {
            TestUtils.createUser(role, '13500000001')
            tag = new Tag(name: '品牌推荐').save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/tags") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = '网红打卡'
            }
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert Tag.count() == 2
        }

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/tags/${tag.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                name = 'updated'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            tag.refresh()
            assert tag.name == 'updated'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/tags/${tag.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Tag.count() == 1
        }

        where:
        role          | should    | postStatus | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 201        | 200       | 204
        'ROLE_SELLER' | 'can not' | 403        | 403       | 403
        'ROLE_YH'     | 'can not' | 403        | 403       | 403
        null          | 'can not' | 401        | 401       | 401
    }

}
