import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:8080";
environment.apiUrl.productService = "http://localhost:8081";
environment.apiUrl.accountService = "http://localhost:8082";

rebuildEnvironment();