package zhusu.backend.operation

import java.time.LocalDateTime

class Post {

    String title
    String content
    boolean published = false
    LocalDateTime dateCreated

    static constraints = {
        title nullable: false
        content nullable: false, maxSize: 500
    }

    static mapping = {
        comment '公告'
        title comment: '标题'
        content comment: '内容'
        published comment: '是否发布'
        dateCreated comment: '发布日期'
    }

}
