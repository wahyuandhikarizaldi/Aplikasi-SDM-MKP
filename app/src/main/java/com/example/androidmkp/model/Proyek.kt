package com.example.androidmkp.model

import android.os.Parcel
import android.os.Parcelable

data class Proyek(
    val kode: String?,
    val proyek: String?,
    val penyelenggara: String?,
    val tahun: String?,
    val lokasi: String?,
    val kota: String?,
    val provinsi: String?,
    val start: String?,
    val end: String?,
    val durasi: String?,
    val totalslot: String?,
    val jumlahpekerja: String?,
    val sisaslot: String?,
    val deskripsi: String?,
    val status: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kode)
        parcel.writeString(proyek)
        parcel.writeString(penyelenggara)
        parcel.writeString(tahun)
        parcel.writeString(lokasi)
        parcel.writeString(kota)
        parcel.writeString(provinsi)
        parcel.writeString(start)
        parcel.writeString(end)
        parcel.writeString(durasi)
        parcel.writeString(totalslot)
        parcel.writeString(jumlahpekerja)
        parcel.writeString(sisaslot)
        parcel.writeString(deskripsi)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Proyek> {
        override fun createFromParcel(parcel: Parcel): Proyek {
            return Proyek(parcel)
        }

        override fun newArray(size: Int): Array<Proyek?> {
            return arrayOfNulls(size)
        }
    }
}

