from SimpleWebSocketServer import SimpleWebSocketServer, WebSocket


class MessageHandler(WebSocket):
    def handleMessage(self):
        # echo message back to client
        print(self.data)
        self.sendMessage(self.data)

    def handleConnected(self):
        print(self.address, 'connected')

    def handleClose(self):
        print(self.address, 'closed')


server = SimpleWebSocketServer('172.23.167.112', 8442, SimpleEcho)
server.serveforever()
