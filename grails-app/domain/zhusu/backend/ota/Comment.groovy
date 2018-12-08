package zhusu.backend.ota

import zhusu.backend.user.User

import java.time.LocalDateTime

class Comment {

    User writer
    Hotel hotel
    int ranking
    String content
    LocalDateTime dateCreated

    static constraints = {
        writer nullable: false
        hotel nullable: false
        ranking min: 1, max: 5
        content nullable: false, blank: false, maxSize: 200
    }

    static mapping = {
        comment '评论'
        version false
        writer comment: '作者'
        hotel comment: '酒店'
        ranking comment: '评分'
        content comment: '内容'
        dateCreated comment: '创建时间'
    }

}
