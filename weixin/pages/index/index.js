//index.js
//获取应用实例
var app = getApp()
Page({
    data: {
        message: '尚未跟手机绑定',
        button: '确认绑定',
        userInfo: {}
    },
    //事件处理函数
    toBind: function () {
        wx.navigateTo({
            url: '../bind/bind'
        })
    },
    doBind: function() {
        console.log(typeof app.sendMessage)
        app.sendMesssage("{}")
    },
    onLoad: function () {
        console.log('onLoad')
        var that = this
        //调用应用实例的方法获取全局数据
        app.getUserInfo(function (userInfo) {
            //更新数据
            console.log(userInfo)
            that.setData({
                userInfo: userInfo
            })
        })
    }
})
