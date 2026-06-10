package bo.bordadoxdanny.app.features.profile.data

import bo.bordadoxdanny.app.features.profile.domain.User

fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    email = email,
    phoneCountryCode = phoneCountryCode,
    phoneNumber = phoneNumber,
    firstName = firstName,
    middleName = middleName,
    lastName1 = lastName1,
    lastName2 = lastName2,
    ciNumber = ciNumber,
    ciComplement = ciComplement,
    ciDepartment = ciDepartment,
    languageCode = languageCode,
    isVerified = isVerified,
    syncStatus = syncStatus,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    email = email,
    phoneCountryCode = phoneCountryCode,
    phoneNumber = phoneNumber,
    firstName = firstName,
    middleName = middleName,
    lastName1 = lastName1,
    lastName2 = lastName2,
    ciNumber = ciNumber,
    ciComplement = ciComplement,
    ciDepartment = ciDepartment,
    languageCode = languageCode,
    isVerified = isVerified,
    syncStatus = syncStatus,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserDto.toEntity() = UserEntity(
    id = id,
    username = username,
    email = email,
    phoneCountryCode = phoneCountryCode,
    phoneNumber = phoneNumber,
    firstName = firstName,
    middleName = middleName,
    lastName1 = lastName1,
    lastName2 = lastName2,
    ciNumber = ciNumber,
    ciComplement = ciComplement,
    ciDepartment = ciDepartment,
    languageCode = languageCode,
    isVerified = true, // If it comes from DTO (server), we assume it's verified or handle it accordingly
    syncStatus = "SYNCED",
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserEntity.toDto() = UserDto(
    id = id,
    username = username,
    email = email,
    phoneCountryCode = phoneCountryCode,
    phoneNumber = phoneNumber,
    firstName = firstName,
    middleName = middleName,
    lastName1 = lastName1,
    lastName2 = lastName2,
    ciNumber = ciNumber,
    ciComplement = ciComplement,
    ciDepartment = ciDepartment,
    languageCode = languageCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)
