package zhusu.backend.ota

class Tag {

    String name

    static constraints = {
        name nullable: false, blank: false, maxSize: 10, unique: true
    }

    static mapping = {
        comment '酒店分类标签'
        version false
        name comment: '名称'
    }

}
