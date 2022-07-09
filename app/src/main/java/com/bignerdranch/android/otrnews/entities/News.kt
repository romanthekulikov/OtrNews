package com.bignerdranch.android.otrnews.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "newsTable")
data class News (
    @SerializedName("id")
    @PrimaryKey
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val image: String,
    @SerializedName("local_img")
    val localImage: String,
    @SerializedName("news_date")
    val newsDate: String,
    @SerializedName("annotation")
    val annotation: String,
    @SerializedName("id_resource")
    val idResource: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("news_date_uts")
    val newsDateUts: String,
    @SerializedName("mobile_url")
    val mobileUrl: String
) : Parcelable