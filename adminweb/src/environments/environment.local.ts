import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:8080";
environment.apiUrl.product = "http://localhost:8081";

rebuildEnvironment();