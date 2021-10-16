package com.example.sthiratestapi.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: Data? = null
)

data class AttendancesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("attendanceList")
	val attendanceList: ArrayList<AttendanceListItem>
)

data class AttendanceListItem(

	@field:SerializedName("in")
	val masuk: In? = null,

	@field:SerializedName("out")
	val pulang: Out? = null
)

data class Out(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long? = null
)

data class Data(

	@field:SerializedName("attendances")
	val attendances: ArrayList<AttendancesItem?>? = null
)

data class In(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Long? = null
)
