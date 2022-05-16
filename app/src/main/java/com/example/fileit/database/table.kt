package com.example.fileit.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

//user table
@Entity(tableName = "user_table",
        indices = [Index(value=[
            "UID"
        ])]
)
    data class User(
        @PrimaryKey
        val UID : Int,
        val user_name : String
    )


//year table
@Entity(tableName = "year", indices = [Index(value = [
    "Year_ID"
    ])])
    data class Year (
        @PrimaryKey
        val Year_ID : Int
    )

//eaform
@Entity(tableName = "ea_form" , foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
    )
])
    data class EAForm (
        @PrimaryKey val F_ID : Int,
        val F_Path : String,
        val F_Description : String,
        val Year_ID : Int
    )

//child table
@Entity(tableName = "child" , foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
    )
])
    data class Child (
        @PrimaryKey val C_ID : Int,
        val C_Path : String,
        val C_Description : String,
        val Year_ID : Int
    )

@Entity(tableName = "insurance", foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
            )
])
    data class Insurance (
    @PrimaryKey val I_ID : Int,
    val I_Path : String,
    val I_Description : String,
    val Year_ID: Int
    )

@Entity(tableName = "lifestyle", foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
            )
])
    data class Lifestyle(
    @PrimaryKey val L_ID : Int,
    val L_Path : String,
    val L_Description : String,
    val Year_ID: Int
    )

@Entity(tableName = "medical", foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Medical(
    @PrimaryKey val M_ID : Int,
    val M_Path : String,
    val M_Description : String,
    val Year_ID: Int
)

@Entity(tableName = "others", foreignKeys = [
    ForeignKey (
        entity = Year::class,
        parentColumns = ["Year_ID"],
        childColumns = ["Year_ID"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Others(
    @PrimaryKey val O_ID : Int,
    val O_Path : String,
    val O_Description : String,
    val Year_ID: Int
)
