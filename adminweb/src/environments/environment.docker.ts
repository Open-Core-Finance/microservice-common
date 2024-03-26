import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:9091";
environment.apiUrl.productService = "http://localhost:9092";
environment.apiUrl.glAccountService = "http://localhost:9093";
environment.apiUrl.depositAccountService = "http://localhost:9094";
environment.apiUrl.loanAccountService = "http://localhost:9095";
environment.apiUrl.customerService = "http://localhost:9096";
environment.apiUrl.goecodeService = "http://localhost:9097";

rebuildEnvironment();
