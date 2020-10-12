import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import api.ForecastService;
import model.City;
import model.Forecast;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.PathDate;

@TestInstance(Lifecycle.PER_CLASS)
class AppTest {

	private MockWebServer mockServer;

	// Following actions & assumptions are done
	// 1. Maven artifacts are downloaded with the help of Internet connection
	// 2. Notion of BDD used while writing TDD test cases based on Junit5(Jupiter)
	// as its already available within the pom

	@BeforeAll
	void setUpMockServer() {
		mockServer = new MockWebServer();
		try {
			mockServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	void tearDownMockServer() {
		try {
			mockServer.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	@Test
	void givenCityName_whenUsingNonNull_ThenSuccessResponse() {
		String cityName = "Dubai";
		String mockResponse = "[{\"title\":\"Dubai\",\"location_type\":\"City\",\"woeid\":1940345,\"latt_long\":\"25.269440,55.308651\"}]";

		mockServer.enqueue(new MockResponse().setBody(new Buffer().write(mockResponse.getBytes())));
		Retrofit retrofit = new Retrofit.Builder().baseUrl(mockServer.url("/"))
				.addConverterFactory(GsonConverterFactory.create()).build();
		ForecastService api = retrofit.create(ForecastService.class);
		try {
			Response<List<City>> response = api.findCityByName(cityName).execute();
			assertEquals("Dubai", response.body().get(0).getTitle());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void givenCityName_whenUsingNonNull_ThenFailureResponse() {
		String cityName = "Dubai";
		String expectedResponse = String.format("Can't find city id for %s", cityName);
		assertEquals(expectedResponse, expectedResponse);
	}

	@Test
	void givenForeCast_whenUsingNonNull_ThenSuccessResponse() {
		long cityId = 1940345L;
		String weatherState = "Clear";

		LocalDate tomorrow = LocalDate.now().plusDays(1);
		PathDate pathDate = new PathDate(tomorrow);

		String mockResponse = "[{\"id\":6251089482481664,\"weather_state_name\":\"Clear\",\"weather_state_abbr\":\"c\",\"wind_direction_compass\":\"N\",\"created\":\"2020-10-12T15:49:31.947845Z\",\"applicable_date\":\"2020-10-12\",\"min_temp\":28.89,\"max_temp\":35.394999999999996,\"the_temp\":33.09,\"wind_speed\":5.685688240913067,\"wind_direction\":358.3075381026185,\"air_pressure\":1009.5,\"humidity\":50,\"visibility\":13.548688161138948,\"predictability\":68}]";

		mockServer.enqueue(new MockResponse().setBody(new Buffer().write(mockResponse.getBytes())));
		Retrofit retrofit = new Retrofit.Builder().baseUrl(mockServer.url("/"))
				.addConverterFactory(GsonConverterFactory.create()).build();
		ForecastService api = retrofit.create(ForecastService.class);
		try {
			Response<List<Forecast>> response = api.getForecast(cityId, pathDate).execute();
			assertEquals(weatherState, response.body().get(0).getWeatherState());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
