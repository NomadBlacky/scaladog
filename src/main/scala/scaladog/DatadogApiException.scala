package scaladog
import requests.Response

class DatadogApiException(message: String, response: Response) extends Exception(message)
