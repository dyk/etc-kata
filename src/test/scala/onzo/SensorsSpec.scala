package onzo

import java.time.{LocalDateTime, LocalTime}

import onzo.data.SensorReadOut
import org.scalatest.{FlatSpec, Matchers}
import squants.energy.KilowattHours

class SensorsSpec extends FlatSpec with Matchers {

  "Sensors" should "read data from file" in {
    Sensors("consumption_data.json") should not be (null)
  }

  it should "reject wrong filename" in {
    assertThrows[Exception] {
      Sensors("wrong_file")
    }
  }

  it should "reject wrong file content" in {
    assertThrows[IllegalArgumentException] {
      Sensors("src/test/resources/wrong.json")
    }
  }

  it should "extract by id" in {
    val sensors = new Sensors(
      List(
        SensorReadOut("12", LocalDateTime.of(2015, 3, 10, 11, 11), KilowattHours(2)),
        SensorReadOut("13", LocalDateTime.of(2015, 3, 10, 11, 11), KilowattHours(2)),
        SensorReadOut("14", LocalDateTime.of(2015, 3, 10, 12, 11), KilowattHours(2))
      ))

    sensors.extractById("12").size should be (1)
    sensors.extractById("no-such-id").size should be (0)
  }

  trait GivenSensors {
    val sensors = Sensors("consumption_data.json").extractById("b08c6195-8cd9-43ab-b94d-e0b887dd73d2")
  }

  it should "Calculate the sensor's total daily consumption in kWh" in new GivenSensors {
    sensors.totalDailyConsuption().toKilowattHours should be (10.713d)
  }

  it should "Calculate the sensor's average hourly consumption from 00:00 to 07:00 inclusive" in new GivenSensors {
    sensors.averageHourlyConsuption(LocalTime.of(0,0), LocalTime.of(7,0)).toKilowattHours should be (0.322875d)
  }

  it should "Calculate the sensor's average hourly consumption from 08:00 to 15:00 inclusive" in new GivenSensors {
    sensors.averageHourlyConsuption(LocalTime.of(8,0), LocalTime.of(15,0)).toKilowattHours should be (0.44925d)
  }

  it should "Calculate the sensor's average hourly consumption from 16:00 to 23:00 inclusive" in new GivenSensors {
    sensors.averageHourlyConsuption(LocalTime.of(16,0), LocalTime.of(23,0)).toKilowattHours should be (0.567d)
  }

}
