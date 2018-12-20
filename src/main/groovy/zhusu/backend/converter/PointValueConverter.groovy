package zhusu.backend.converter

import grails.databinding.converters.ValueConverter
import zhusu.backend.ota.Hotel

class PointValueConverter implements ValueConverter {

    boolean canConvert(value) {
        value instanceof String
    }

    def convert(value) {
        def pieces = value.split(';')
        new Hotel(lat: pieces[0], lng: pieces[1])
    }

    Class<?> getTargetType() {
        Hotel
    }
}
