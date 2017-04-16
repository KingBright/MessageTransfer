from SimpleWebSocketServer import SimpleWebSocketServer, WebSocket
import messagehandler


class MessageHandler(WebSocket):
    def handleMessage(self):
        print('received message', self.data)
        try:
            messagehandler.handle(self, self.data)
        except Exception as exc:
            print(exc)

    def handleConnected(self):
        print(self.address, 'connected')

    def handleClose(self):
        print(self.address, 'closed')


server = SimpleWebSocketServer('192.168.199.200', 8442, MessageHandler)
server.serveforever()
