import VerificationService
import DbService


def check_phone_bind_state(message):
    try:
        phone, result = DbService.get_phone(message)
        if phone.bind_state is not True:
            code = VerificationService.get_verification_code(4)
            phone.set_bind_code(code)
            print("please verify quickly")
            return code, "Please submit the verification code on your WeiXin."
        else:
            print("already binded")
            return -1, "Your phone has already been binded."
    except Exception as exc:
        print(exc)
        return -1, "Oops, this phone can not be registered."


def check_weixin_bind_state(message):
    try:
        phone, result = DbService.get_phone(message)
        if phone.bind_state is not True:
            code = VerificationService.get_verification_code(4)
            phone.set_bind_code(code)
            print("please verify quickly")
            return code, "Please submit the verification code on your WeiXin."
        else:
            print("already binded")
            return -1, "Your phone has already been binded."
    except Exception as exc:
        print(exc)
        return -1, "Oops, this phone can not be registered."
