import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:9091";
environment.apiUrl.productService = "http://localhost:9092";
environment.apiUrl.glAccountService = "http://localhost:9092";
environment.apiUrl.depositAccountService = "http://localhost:9092";
environment.apiUrl.loanAccountService = "http://localhost:9092";
environment.apiUrl.customerService = "http://localhost:9092";

rebuildEnvironment();
