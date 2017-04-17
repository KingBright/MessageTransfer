from MessageTransferProtocal import *
import MessageFactory
import BindService
import SmsService


def handle_bind_request(send, message):
    bind_message = MessageFactory.get_bind_message(message)
    code, msg = BindService.check_bind_state(bind_message)
    response_message = MessageFactory.build_bind_response_message(code, msg)
    send(response_message)


def handle_sms_request(send, message):
    result, msg = SmsService.add_new_sms(message)
    if result:
        code = 0
    else:
        code = -1

    response_message = MessageFactory.build_sms_response_message(message.sign, msg, code)
    send(response_message)


def handle(send, data):
    wrapper_message = MessageFactory.get_wrapper_message(data)
    print('type', wrapper_message.type)
    print('source', wrapper_message.source)

    message_type = Type.get_type(wrapper_message.type)
    if message_type is Type.Bind:
        handle_bind_request(send, MessageFactory.get_bind_message(wrapper_message.message))
        return
    elif message_type is Type.Sms:
        handle_sms_request(send, MessageFactory.get_sms_message(wrapper_message.message))
        return
    else:
        print("unrecognized type:", message_type)
