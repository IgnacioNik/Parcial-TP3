package com.example.myapplication.data.models

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val mobile: String,
    val dob: String, // Date of Birth
    val password: String
)