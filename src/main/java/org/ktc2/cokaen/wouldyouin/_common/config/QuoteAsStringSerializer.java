package org.ktc2.cokaen.wouldyouin._common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class QuoteAsStringSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        JsonStringEncoder encoder = JsonStringEncoder.getInstance();
        char[] escapedJson = encoder.quoteAsString(value);
        String escapedString = String.valueOf(escapedJson).replace("\\\\n", "\n");
        gen.writeString(escapedString);

    }
}