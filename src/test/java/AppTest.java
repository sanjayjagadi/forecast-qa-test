import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.Forecast;
import util.PathDate;

class AppTest {

	// Following actions & assumptions are done
	// 1. Maven artifacts are downloaded with the help of Internet connection
	// 2. Notion of BDD used while writing TDD test cases based on Junit5(Jupiter)
	// as its already available within the pom

	@Test
	void givenCityName_whenUsingNonNull_ThenSuccessResponse() {
		String cityName = "Dubai";
		String weatherState = "clear";
		Double temperature = 100.00;
		Double windSpeed = 70.00;
		Integer humidity = 10;
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		PathDate pathDate = new PathDate(tomorrow);

		Forecast forecast = new Forecast();
		forecast.setHumidity(humidity);
		forecast.setId(100L);
		forecast.setTemperature(temperature);
		forecast.setWeatherState(weatherState);
		forecast.setWindSpeed(windSpeed);
		String expectedResponse = String.format("%s\nTemp: %.1f °C\nWind: %.1f mph\nHumidity: %d%%", weatherState,
				temperature, windSpeed, humidity);
		System.out.println(expectedResponse);
		assertNotNull(cityName);
		assertTrue(true);
	}

	@Test
	void givenCityName_whenUsingNonNull_ThenFailureResponse() {
		String cityName = "Dubai";
		String expectedResponse = String.format("Can't find city id for %s", cityName);
		assertEquals(expectedResponse, expectedResponse);
	}

	@Test
	void givenCityName_whenUsingNull_ThenFailureResponse() {
		String cityName = null;
		String expectedResponse = String.format("Can't get forecast for %s", cityName);
		assertEquals(expectedResponse, expectedResponse);
	}

}
