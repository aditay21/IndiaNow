package com.kaushaltechnology.india.utils

import retrofit2.HttpException


enum class AppError(val code: Int, val message: String) {
    NO_INTERNET(1, "No Internet Connection"),
    NETWORK_ERROR(2, "Network Error"),
    INVALID_ARGUMENT(3, "Invalid argument provided"),
    SECURITY_ERROR(4, "Security error: Unauthorized access detected"),
    UNEXPECTED_ERROR(5, "Unexpected Error");

    companion object {
        fun getCodeFromException(exception: Throwable): Int? {
            return when (exception) {
                is java.net.UnknownHostException -> NO_INTERNET.code
                is java.io.IOException -> NETWORK_ERROR.code
                is HttpException -> ApiError.fromCode(exception.code()).code
                is IllegalArgumentException -> INVALID_ARGUMENT.code
                is SecurityException -> SECURITY_ERROR.code
                else -> UNEXPECTED_ERROR.code
            }
        }

        fun getMessageFromCode(errorCode: Int): String {
            return when (errorCode) {
                AppError.NO_INTERNET.code -> AppError.NO_INTERNET.message
                AppError.NETWORK_ERROR.code -> AppError.NETWORK_ERROR.message
                AppError.INVALID_ARGUMENT.code -> AppError.INVALID_ARGUMENT.message
                AppError.SECURITY_ERROR.code -> AppError.SECURITY_ERROR.message
                AppError.UNEXPECTED_ERROR.code -> AppError.UNEXPECTED_ERROR.message
                else -> "Unknown error occurred. Please try again."
            }
        }
    }
}
