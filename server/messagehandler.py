from MessageTransferProtocal import *
import MessageFactory
import BindService
import SmsService


def handle_phone_bind_request(send, message):
    code, msg = BindService.check_phone_bind_state(message)
    response_message = MessageFactory.build_bind_response_message(code, msg)
    send(response_message)


def handle_weixin_bind_request(send, message):
    code, msg = BindService.check_weixin_bind_state(message)
    response_message = MessageFactory.build_bind_response_message(code, msg)
    send(response_message)


def handle_sms_request(send, message):
    result, msg = SmsService.add_new_sms(message)
    if result:
        code = 0
    else:
        code = -1

    response_message = MessageFactory.build_sms_response_message(message.sign, code, msg)
    send(response_message)


def handle(send, data):
    wrapper_message = MessageFactory.get_wrapper_message(data)
    print('type', wrapper_message.type)
    print('source', wrapper_message.source)

    message_type = Type.get_type(wrapper_message.type)
    message_source = Source.get_source(wrapper_message.source)
    if message_type is Type.Bind:
        if message_source == Source.Phone:
            handle_phone_bind_request(send,
                                      MessageFactory.get_bind_message(wrapper_message.message))
        elif message_source == Source.WeiXin:
            handle_weixin_bind_request(send,
                                       MessageFactory.get_bind_message(wrapper_message.message))
    elif message_type is Type.Sms:
        if message_source is Source.Phone:
            handle_sms_request(send, MessageFactory.get_sms_message(wrapper_message.message))
        elif message_source is Source.WeiXin:
            pass
    else:
        print("unrecognized type:", message_type)
