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
