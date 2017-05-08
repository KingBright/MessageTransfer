//app.js
var util = require('utils/util.js')
var mq = require('ws/websocket.js')
App({
    onLaunch: function () {
        this.checkSession()
    },
    checkSession: function () {
        var that = this
        wx.checkSession({
            success: function () {
                console.log("check session success")
                // that.getUserInfo()
                wx.login({
                    success: function (data) {
                        console.log("login", data)
                        wx.setStorageSync('code', data.code)
                        that.getUserInfo()
                    }
                })
            },
            fail: function () {
                console.log("check session fail")
                wx.login({
                    success: function (data) {
                        console.log("login", data)
                        that.getUserInfo()
                    }
                })
            }
        })
    },
    getUserInfo: function (cb) {
        var that = this
        if (this.globalData.userInfo) {
            util.callback(cb, this.globalData.userInfo)
        } else {
            wx.getUserInfo({
                withCredentials: true,
                success: function (res) {
                    that.requestSession(res)

                    that.globalData.userInfo = res.userInfo
                    util.callback(cb, that.globalData.userInfo)
                }
            })
        }
    },
    requestSession: function (res) {
        mq.enqueue({
            code: 11,
            source: 1,
            message: {
                code: wx.getStorageSync('code'),
                rawData: res.rawData,
                signature: res.signature,
                encryptedData: res.encryptedData,
                iv: res.iv
            }
        })
    },
    enqueueMsg: function (msg) {
        mq.enqueue(msg)
    },
    checkBindState: function (cb) {
        var state = wx.getStorageSync('bind') || []
        util.callback(cb, state)
    },
    globalData: {
        userInfo: null
    }
})