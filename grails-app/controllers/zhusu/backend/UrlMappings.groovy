package zhusu.backend

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action: "delete")
        get "/$controller(.$format)?"(action: "index")
        get "/$controller/$id(.$format)?"(action: "show")
        post "/$controller(.$format)?"(action: "save")
        put "/$controller/$id(.$format)?"(action: "update")
        patch "/$controller/$id(.$format)?"(action: "patch")

        "/"(controller: 'application', action: 'index')
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/api/register"(controller: 'User', action: 'register')
        "/api/self"(controller: 'User', action: 'self')
        "/api/updatePersonalInfo"(controller: 'User', action: 'updatePersonalInfo')
        "/api/changePassword"(controller: 'User', action: 'changePassword')
        "/api/sendSmsCode"(controller: 'User', action: 'sendSmsCode')
        "/api/users"(resources: 'Management')
        "/api/users/resetPassword"(controller: 'Management', action: 'resetPassword')
        get "/api/getUploadAuthority"(controller: 'AliyunOSS', action: 'getUploadAuthority')
        "/api/posts"(resources: 'Post')
        "/api/comments"(resources: 'Comment')
        "/api/comments/listByHotel"(controller: 'Comment', action: 'listByHotel')
        "/api/comments/listByUser"(controller: 'Comment', action: 'listByUser')
        "/api/comments/listByRanking"(controller: 'Comment', action: 'listByRanking')
        "/api/hotels"(resources: 'Hotel')
        "/api/rooms"(resources: 'Room')
        "/api/tags"(resources: 'Tag')
        "/api/orders"(resources: 'Order')
        "/api/orders/order"(controller: 'Order', action: 'order')
        "/api/orders/confirm"(controller: 'Order', action: 'confirm')
        "/api/orders/checkIn"(controller: 'Order', action: 'checkIn')
        "/api/orders/checkOut"(controller: 'Order', action: 'checkOut')
        "/api/orders/cancel"(controller: 'Order', action: 'cancel')
        "/api/orders/getCountByDate"(controller: 'Order', action: 'getCountByDate')
        "/api/orderExecutions"(resources: 'OrderExecution')
        "/api/orderExecutions/last"(controller: 'OrderExecution', action: 'last')
    }
}
