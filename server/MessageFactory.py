from MessageTransferProtocal import *
import Pson


def get_wrapper_message(json):
    wrapper_message = Pson.to_obj(json, WrapperMessage)
    return wrapper_message


def get_bind_message(json):
    bind_message = Pson.to_obj(json, BindMessage)
    return bind_message


def get_sms_message(json):
    sms_message = Pson.to_obj(json, SmsMessage)
    return sms_message


def build_bind_response_message(code, msg=""):
    bind_out_message = BindResponseMessage(code, msg)
    wrapper_message = WrapperMessage()
    wrapper_message.type = Type.Bind_Response.value
    wrapper_message.message = Pson.to_json(bind_out_message)
    wrapper_message.source = Source.Server.value
    return Pson.to_json(wrapper_message)


def build_sms_response_message(sign, code, msg=""):
    sms_out_message = SmsResponseMessage(sign, code, msg)
    wrapper_message = WrapperMessage()
    wrapper_message.type = Type.Sms_Response.value
    wrapper_message.message = Pson.to_json(sms_out_message)
    wrapper_message.source = Source.Server.value
    return Pson.to_json(wrapper_message)
