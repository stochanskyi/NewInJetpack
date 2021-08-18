package com.mars.newinjetpack.data.datastore.proto.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.mars.newinjetpack.UserData
import java.io.InputStream
import java.io.OutputStream

class UserDataSerializer : Serializer<UserData> {
    override val defaultValue: UserData = UserData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserData {
        return try {
            UserData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: UserData, output: OutputStream) {
        t.writeTo(output)
    }

}