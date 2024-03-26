import { environment as defaultEnvironment, rebuildEnvironment } from './environment.defaults';

export const environment = {
    ...defaultEnvironment,
    production: false
};

environment.apiUrl.userprofile = "http://localhost:8080";
environment.apiUrl.productService = "http://localhost:8081";
environment.apiUrl.glAccountService = "http://localhost:8082";
environment.apiUrl.depositAccountService = "http://localhost:8083";
environment.apiUrl.loanAccountService = "http://localhost:8085";
environment.apiUrl.customerService = "http://localhost:8084";
environment.apiUrl.goecodeService = "http://localhost:8086";

rebuildEnvironment();