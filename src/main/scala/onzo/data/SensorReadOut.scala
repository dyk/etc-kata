package onzo.data

import java.time.{LocalDateTime, LocalTime}
import java.time.format.DateTimeFormatter

import io.circe.Decoder
import squants.Energy
import squants.energy.WattHours

case class SensorReadOut(sensorId: String,
                        timestamp: LocalDateTime,
                        consumption: Energy) {

  def between(from: LocalTime, to: LocalTime): Boolean =
    timestamp.toLocalTime.compareTo(from) >= 0 && timestamp.toLocalTime.compareTo(to) <= 0
}


object SensorReadOut {

  object decoder {

    implicit val dt = Decoder.decodeString.map(s => LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
    implicit val wh = Decoder.decodeInt.map(WattHours(_))
    implicit val sro = Decoder.forProduct3("sensor_id", "timestamp", "consumption_Wh")(SensorReadOut.apply)

  }

}

