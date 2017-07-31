package onzo

import java.time.LocalTime

import onzo.data.SensorReadOut
import squants.energy.WattHours

class Sensors(private val data: List[SensorReadOut] = Nil) {
  val size = data.size
  def averageHourlyConsuption(from: LocalTime, to: LocalTime) = {
    val fromRange = data.filter(_.between(from, to))
    if (fromRange.isEmpty) {
      WattHours(0)
    } else {
      Sensors(fromRange).totalDailyConsuption() / fromRange.size
    }
  }
  def totalDailyConsuption() = data.foldLeft(WattHours(0))(_ + _.consumption)
  def extractById(id: String): Sensors = Sensors(data.filter(_.sensorId == id))
}

object Sensors {

  def apply(data: List[SensorReadOut]): Sensors = new Sensors(data)
  def apply(dataFileName: String): Sensors = {

    import scala.io.Source
    import io.circe.parser._
    import SensorReadOut.decoder._

    (for {
      json <- parse(Source.fromFile(dataFileName).mkString)
      readOuts <- json.as[List[SensorReadOut]]
    } yield Sensors(readOuts)) match {
      case Right(s) => s
      case _ => throw new IllegalArgumentException(s"cannot read file $dataFileName")
    }

  }
}
