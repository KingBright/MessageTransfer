var state = "Waiting"
var callback = null
var msgQueue = []
function sendMessage(msg) {
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
            state = "Open"
            var queue = msgQueue
            msgQueue = []
            for (var i = 0; i < queue.length; i++) {
                sendMessageInner(queue[i])
            }
        })
        wx.onSocketClose(function () {
            console.log("socket close")
            state = "Close"
        })
        wx.onSocketError(function () {
            console.log("socket error")
            state = "Error"
        })
        wx.onSocketMessage(function (data) {
            console.log("message received", data)
            util.callback(callback, data)
        })
    }

    if (state == "Open") {
        sendMessageInner(msg)
    } else {
        enqueue(msg)
        connectSocket()
    }
}
function onReceive(cb) {
    callback = cb
}

module.exports = {
    enqueue: sendMessage,
    onReceive: onReceive
}