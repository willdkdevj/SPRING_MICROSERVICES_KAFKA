package br.com.supernova.ecommercemicroservice.streaming;

import br.com.supernova.ecommercemicroservice.entity.enums.StatusCheckoutEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = CheckoutEventStream.CheckoutEventStreamBuilder.class)
@Builder(builderClassName = "CheckoutEventStreamBuilder", toBuilder = true)
public class CheckoutEventStream {

    String checkoutCode;
    StatusCheckoutEnum status;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CheckoutEventStreamBuilder {}

}
