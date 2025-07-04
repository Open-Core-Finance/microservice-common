package tech.corefinance.kafka.common.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Headers;
import org.springframework.http.HttpHeaders;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.context.TenantContext;
import tech.corefinance.common.context.TraceIdContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static tech.corefinance.common.enums.CommonConstants.*;

@Slf4j
public class CommonKafkaInterceptor implements ProducerInterceptor<String, Object>, ConsumerInterceptor<String, Object> {

    public void applyCommonHeader(Headers headers) {
        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        var tenantId = TenantContext.getInstance().getTenantId();
        log.debug("Tenant ID [{}]", tenantId);
        checkNullAndAddHeader(headers, HEADER_KEY_TENANT_ID, tenantId);
        var traceId = TraceIdContext.getInstance().getTraceId();
        log.debug("Trace ID [{}]", tenantId);
        checkNullAndAddHeader(headers, HEADER_KEY_TRACE_ID, traceId);
        if (jwtTokenDto != null) {
            log.debug("Found JWT token in context!");
            checkNullAndAddHeader(headers, HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtTokenDto.getOriginalToken()).checkNullAndAddHeader(
                            headers, DEVICE_ID, jwtTokenDto.getDeviceId())
                    .checkNullAndAddHeader(headers, HEADER_KEY_EXTERNAL_IP_ADDRESS, jwtTokenDto.getLoginIpAddr());
        } else {
            log.debug("Didn't found JWT token in context!");
        }
    }

    private CommonKafkaInterceptor checkNullAndAddHeader(Headers headers, String key, String value) {
        if (value != null) {
            headers.add(key, value.getBytes(StandardCharsets.UTF_8));
        }
        return this;
    }

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> producerRecord) {
        applyCommonHeader(producerRecord.headers());
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public ConsumerRecords<String, Object> onConsume(ConsumerRecords<String, Object> consumerRecords) {
        JwtService jwtService = ApplicationContextHolder.getInstance().getApplicationContext().getBean(JwtService.class);
        consumerRecords.forEach(record -> {
            log.debug("Received Message from topic [{}], key [{}], value [{}]", record.topic(), record.key(), record.value());
            var headers = record.headers();
            var tenantIdVal = extractLastStringHeaderValue(headers, HEADER_KEY_TENANT_ID);
            log.debug("Tenant ID from message header [{}]", tenantIdVal);
            TenantContext.getInstance().setTenantId(tenantIdVal);
            var traceIdVal = extractLastStringHeaderValue(headers, HEADER_KEY_TENANT_ID);
            log.debug("Trace ID from message header [{}]", traceIdVal);
            TraceIdContext.getInstance().setTraceId(traceIdVal);
            var authenTokenVal = extractLastStringHeaderValue(headers, HttpHeaders.AUTHORIZATION);
            var ipAddressVal = extractLastStringHeaderValue(headers, HEADER_KEY_EXTERNAL_IP_ADDRESS);
            var deviceIdVal = extractLastStringHeaderValue(headers, DEVICE_ID);
            log.debug("Token from message header [{}]", authenTokenVal);
            if (authenTokenVal != null) {
                authenTokenVal = authenTokenVal.substring(BEARER_PREFIX.length()).trim();
                try {
                    var jwtToken = jwtService.decodeToken(authenTokenVal, deviceIdVal, ipAddressVal);
                    JwtContext.getInstance().setJwt(jwtToken);
                } catch (JsonProcessingException e) {
                    log.error("Error when extract JWT from kafka message. {}", e.getMessage(), e);
                }
            }
        });
        return consumerRecords;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {
    }

    private String extractLastStringHeaderValue(Headers headers, String key) {
        var header = headers.lastHeader(key);
        if (header != null) {
            var val = header.value();
            if (val != null) {
                return new String(val, StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
