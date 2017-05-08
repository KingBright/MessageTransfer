var app = getApp()
Page({
    data: {},
    doBind: function() {
        bindInfo ={
            type:21,
            source:1,
            message:{}
        }
        app.enqueueMsg("{}")
    },
    onLoad: function () {
        this.setData({})
    } 
})