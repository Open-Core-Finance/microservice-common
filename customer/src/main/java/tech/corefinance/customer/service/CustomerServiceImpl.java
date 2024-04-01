package tech.corefinance.customer.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.customer.entity.Customer;
import tech.corefinance.feign.client.geocode.CityClient;
import tech.corefinance.feign.client.geocode.CountryClient;
import tech.corefinance.feign.client.geocode.StateClient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class CustomerServiceImpl {

    protected CityClient cityClient;
    protected TaskExecutor taskExecutor;
    protected StateClient stateClient;
    protected CountryClient countryClient;

    public void validateCreateCustomer(Customer dest) {
        if (dest.isMailingSameWithAddress()) {
            dest.setMailingDistrict(dest.getDistrict());
            dest.setMailingStreetAddressLine1(dest.getStreetAddressLine1());
            dest.setMailingStreetAddressLine2(dest.getStreetAddressLine2());
            dest.setMailingZipPostalCode(dest.getZipPostalCode());
        }
        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        List<CompletableFuture<?>> tasks = new LinkedList<>();
        tasks.add(CompletableFuture.supplyAsync(() -> {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            int cityId = dest.getCityId();
            var cityResult = cityClient.viewDetails(cityId).getResult();
            if (cityResult != null) {
                dest.setCity(cityResult.getName());
                if (dest.isMailingSameWithAddress()) {
                    dest.setMailingCity(dest.getCity());
                    dest.setMailingCityId(dest.getCityId());
                }
            }
            return cityResult;
        }, taskExecutor));
        tasks.add(CompletableFuture.supplyAsync(() -> {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            int stateId = dest.getStateId();
            var stateResult = stateClient.viewDetails(stateId).getResult();
            if (stateResult != null) {
                dest.setState(stateResult.getName());
                if (dest.isMailingSameWithAddress()) {
                    dest.setMailingState(dest.getCity());
                    dest.setMailingStateId(dest.getStateId());
                }
            }
            return stateResult;
        }, taskExecutor));
        tasks.add(CompletableFuture.supplyAsync(() -> {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            int countryId = dest.getCountryId();
            var countryResult = countryClient.viewDetails(countryId).getResult();
            if (countryResult != null) {
                dest.setCountry(countryResult.getName());
                if (dest.isMailingSameWithAddress()) {
                    dest.setMailingCountry(dest.getCountry());
                    dest.setMailingCountryId(dest.getCountryId());
                }
            }
            return countryResult;
        }, taskExecutor));
        for (var task : tasks) {
            task.join();
        }
    }
}
