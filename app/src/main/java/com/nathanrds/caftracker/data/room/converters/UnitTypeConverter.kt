package com.nathanrds.caftracker.data.room.converters

import androidx.room.TypeConverter
import com.nathanrds.caftracker.domain.model.UnitType

class UnitTypeConverter {
    @TypeConverter
    fun fromUnitType(unitType: UnitType): String {
        return unitType.name
    }

    @TypeConverter
    fun toUnitType(unitTypeString: String?): UnitType {
        return runCatching {
            UnitType.valueOf(unitTypeString.orEmpty())
        }.getOrDefault(UnitType.ML)
    }
}
