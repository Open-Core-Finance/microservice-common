import { environment as defaultEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://userprofile.corefinance.tech";
environment.apiUrl.authentication = environment.apiUrl.userprofile + "/authentication";
environment.apiUrl.product = "http://product.corefinance.tech";
environment.apiUrl.organization = environment.apiUrl.product + "/organizations";
environment.apiUrl.currency = environment.apiUrl.product + "/currencies";

// environment.apiUrl.user = environment.apiUrlRoot + "/user";
// environment.apiUrl.subject = environment.apiUrlRoot + "/subject";
// environment.apiUrl.organization = environment.apiUrlRoot + "/organization";
// environment.apiUrl.role = environment.apiUrlRoot + "/role";
// environment.apiUrl.registration = environment.apiUrlRoot + "/registration";
// environment.apiUrl.period = environment.apiUrlRoot + "/period";
// environment.apiUrl.klass = environment.apiUrlRoot + "/klass";
// environment.apiUrl.feed = environment.apiUrlRoot + "/feed";
// environment.apiUrl.course = environment.apiUrlRoot + "/course";
// environment.apiUrl.common = environment.apiUrlRoot + "/common";