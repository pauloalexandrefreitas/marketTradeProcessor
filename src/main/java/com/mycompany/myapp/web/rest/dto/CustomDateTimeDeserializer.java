package com.mycompany.myapp.web.rest.dto;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

	private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final SimpleDateFormat CUSTOM_FORMATTER = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");

	@Override
	public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
			String string = parser.getText().trim();
			if (string.length() == 0) {
				return null;
			}
			try {
				return CUSTOM_FORMATTER.parse(string).toInstant().atZone(ZoneId.systemDefault());
			} catch (DateTimeException | ParseException e) {
				// TODO Auto-generated catch block
			}
			try {
				// JavaScript by default includes time and zone in JSON serialized Dates (UTC/ISO instant format).
				if (string.length() > 10 && string.charAt(10) == 'T') {
					if (string.endsWith("Z")) {
						return ZonedDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
					} else {
						return ZonedDateTime.parse(string, DEFAULT_FORMATTER);
					}
				}
				return ZonedDateTime.parse(string, DEFAULT_FORMATTER);
			} catch (DateTimeException e) {
				// TODO Auto-generated catch block
			}
		}

		return null;
	}

}
