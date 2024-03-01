import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:9091";
environment.apiUrl.product = "http://localhost:9092";

rebuildEnvironment();
