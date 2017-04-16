import VerificationService
import DbService


def check_bind_state(message):
    try:
        phone = DbService.get_phone(message)
        if phone.bind_state is not True:
            code = VerificationService.get_verification_code(4)
            phone.set_bind_code(code)
            return code, "Please confirm the verification code on your WeiXin."
        else:
            return 0, "Already binded."
    except:
        return 0, "Oops, this phone can not be registered."
