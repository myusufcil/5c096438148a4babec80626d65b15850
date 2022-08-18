package com.myusufcil.core.enum

enum class ErrorMessages(var errorMessage:String) {
    EUSNotEnough(errorMessage = "Yeterli EUS bulunmadığı için dünyaya gönderildiniz."),
    UGSNotEnough(errorMessage = "Yeterli UGS bulunmadığı için dünyaya gönderildiniz."),
    DurabilityNotEnough(errorMessage = "Hasar Kapasitenizi bulunmadığı için dünyaya gönderildiniz."),
    TimeNotEnough(errorMessage = "Zamanınız bittiği için  dünyaya gönderildiniz.")
}