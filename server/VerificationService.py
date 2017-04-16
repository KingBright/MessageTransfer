import random

sample = [str(num) for num in range(0, 10)] + [chr(num) for num in range(65, 91)] + [chr(num) for num in
                                                                                     range(97, 123)]


def get_verification_code(number):
    verification_code = "".join(random.sample(sample, number))
    return verification_code
