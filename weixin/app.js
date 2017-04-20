//app.js
var util = require('utils/util.js')
App({
    onLaunch: function () {
        //调用API从本地缓存中获取数据
        var logs = wx.getStorageSync('logs') || []
        logs.unshift(Date.now())
        wx.setStorageSync('logs', logs)
    },
    getUserInfo: function (cb) {
        var that = this
        if (this.globalData.userInfo) {
            util.callback(cb, this.globalData.userInfo)
        } else {
            //调用登录接口
            wx.login({
                success: function () {
                    wx.getUserInfo({
                        success: function (res) {
                            console.log("res", res)
                            that.globalData.userInfo = res.userInfo
                            util.callback(cb, that.globalData.userInfo)
                        }
                    })
                }
            })
        }
    },
    checkBindState: function (cb) {
        var state = wx.getStorageSync('bind') || []
        util.callback(cb, state)
    },
    state: "Waiting",
    callback: null,
    msgQueue: [],
    sendMessage: function (msg) {
        var enqueue = function (msg) {
            console.log("enqueue msg", msg)
            msgQueue.push(msg)
        }

        var sendMessageInner = function (msg) {
            console.log("send message inner", msg)
            wx.sendSocketMessage({
                data: msg,
                success: function (res) {
                    console.log("successfully send msg", msg)
                },
                fail: function (res) {
                    console.log("failed send msg", msg)
                    enqueue(msg)
                }
            })
        }

        var connectSocket = function () {
            console.log("try to connect WebSocket")
            wx.connectSocket({
                url: "wss://ssl.kingbright.name/ws"
            })
            wx.onSocketOpen(function () {
                console.log("socket open")
                this.state = "Open"
                var queue = this.msgQueue
                this.msgQueue = []
                for (var i = 0; i < queue.length; i++) {
                    sendMessageInner(queue[i])
                }
            })
            wx.onSocketClose(function () {
                console.log("socket close")
                this.state = "Close"
            })
            wx.onSocketError(function () {
                console.log("socket error")
                this.state = "Error"
            })
            wx.onSocketMessage(function (data) {
                console.log("message received", data)
                util.callback(this.callback, data)
            })
        }

        if (state == "Open") {
            sendMessageInner(msg)
        } else {
            enqueue(msg)
            connectSocket()
        }
    },
    onMessage: function (cb) {
        this.callback = cb
    },
    globalData: {
        userInfo: null
    }
})