from SimpleWebSocketServer import SimpleWebSocketServer, WebSocket


class SimpleEcho(WebSocket):
    def handleMessage(self):
        # echo message back to client
        print(self.data)
        self.sendMessage(self.data)

    def handleConnected(self):
        print(self.address, 'connected')

    def handleClose(self):
        print(self.address, 'closed')


server = SimpleWebSocketServer('192.168.199.200', 8442, SimpleEcho)
server.serveforever()
