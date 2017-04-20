var app = getApp()
Page({
    data: {},
    doBind: function() {
        console.log(typeof app.sendMessage)
        app.sendMesssage("{}")
    },
    onLoad: function () {
        this.setData({})
    } 
})