package zhusu.backend.operation

import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Specification
import spock.lang.Unroll
import zhusu.backend.utils.TestUtils

@Integration
@Rollback
class PostFunctionalSpec extends Specification {

    PostService postService

    void setup() {
        TestUtils.initEnv()
    }

    void cleanup() {
        TestUtils.clearEnv()
    }

    @Unroll
    void "#role 可以看到 #tag 广告列表"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Post.withNewTransaction {
            if (role) {
                TestUtils.createUser(role, '13500000001')
            }
            new Post(title: 'title', content: 'content', published: true).save()
            new Post(title: 'title', content: 'content', published: false).save()
        }

        when:
        String jwt
        if(role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/posts") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.json.postCount == count

        where:
        role          | tag                        | count
        'ROLE_ADMIN'  | 'published = true + false' | 2
        'ROLE_SELLER' | 'published = true'         | 1
        'ROLE_YH'     | 'published = true'         | 1
        null          | 'published = true'         | 1
    }

    @Unroll
    void "#role 访问 published = #published 的广告，返回状态码：#status"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Post post
        Post.withNewTransaction {
            if (role) {
                TestUtils.createUser(role, '13500000001')
            }
            post = new Post(title: 'title', content: 'content', published: published).save()
        }

        when:
        String jwt
        if(role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.get("http://localhost:${serverPort}/api/posts/${post.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == status

        where:
        role          | published | status
        'ROLE_ADMIN'  | true      | 200
        'ROLE_ADMIN'  | false     | 200
        'ROLE_SELLER' | true      | 200
        'ROLE_SELLER' | false     | 403
        'ROLE_YH'     | true      | 200
        'ROLE_YH'     | false     | 403
        null          | true      | 200
        null          | false     | 403
    }

    @Unroll
    void "#role #should 创建、编辑、删除广告"() {
        setup:
        RestBuilder rest = new RestBuilder()
        RestResponse response
        Post post
        Post.withNewTransaction {
            TestUtils.createUser(role, '13500000001')
            post = new Post(title: 'title', content: 'content').save()
        }
        String jwt

        when: 'save'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.post("http://localhost:${serverPort}/api/posts") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                title = 'title'
                content = 'content'
            }
        }

        then:
        response.status == postStatus
        if (postStatus == 201) {
            assert Post.count() == 2
        }

        when: 'update'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.put("http://localhost:${serverPort}/api/posts/${post.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
            json {
                title = 'updated'
                content = 'updated'
            }
        }

        then:
        response.status == putStatus
        if (putStatus == 200) {
            post.refresh()
            assert post.title == 'updated'
            assert post.content == 'updated'
        }

        when: 'delete'
        if (role) {
            jwt = TestUtils.login(serverPort, '13500000001', '13500000001')
        }
        response = rest.delete("http://localhost:${serverPort}/api/posts/${post.id}") {
            if (role) {
                header('Authorization', "Bearer ${jwt}")
            }
        }

        then:
        response.status == deleteStatus
        if (deleteStatus == 204) {
            assert Post.count() == 1
        }

        where:
        role          | should    | postStatus | putStatus | deleteStatus
        'ROLE_ADMIN'  | 'can'     | 201        | 200       | 204
        'ROLE_SELLER' | 'can not' | 403        | 403       | 403
        'ROLE_YH'     | 'can not' | 403        | 403       | 403
        null          | 'can not' | 401        | 401       | 401
    }

}
